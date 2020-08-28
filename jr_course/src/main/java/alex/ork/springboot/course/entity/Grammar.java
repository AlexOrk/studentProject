package alex.ork.springboot.course.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="grammar")
public class Grammar {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="jlpt_lvl")
    private String jlptLvl;

    @Column(name="formula")
    private String formula;

    @Column(name="example")
    private String example;

    @Column(name="description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                                                   CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "grammar_users_data",
            joinColumns = @JoinColumn(name = "grammar_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    private Collection<UsersData> usersDataCollection;

    public Grammar() {}

    public Grammar(String jlptLvl, String formula, String example, String description) {
        this.jlptLvl = jlptLvl;
        this.formula = formula;
        this.example = example;
        this.description = description;
    }

    public Grammar(String jlptLvl, String formula, String example, String description,
                   Collection<UsersData> usersDataCollection) {
        this.jlptLvl = jlptLvl;
        this.formula = formula;
        this.example = example;
        this.description = description;
        this.usersDataCollection = usersDataCollection;
    }

    public void addUsersData(UsersData usersData) {
        if (usersDataCollection == null)
            usersDataCollection = new ArrayList<>();
        usersDataCollection.add(usersData);
    }

    public void deleteUsersData(UsersData usersData) {
        if (usersDataCollection != null)
            usersDataCollection.remove(usersData);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJlptLvl() {
        return jlptLvl;
    }

    public void setJlptLvl(String jlptLvl) {
        this.jlptLvl = jlptLvl;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<UsersData> getUsersDataCollection() {
        return usersDataCollection;
    }

    public void setUsersDataCollection(Collection<UsersData> usersDataCollection) {
        this.usersDataCollection = usersDataCollection;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "id=" + id +
                ", jlptLvl='" + jlptLvl + '\'' +
                ", formula='" + formula + '\'' +
                ", example='" + example + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

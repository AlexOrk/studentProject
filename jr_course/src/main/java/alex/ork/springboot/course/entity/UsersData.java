package alex.ork.springboot.course.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="users_data")
public class UsersData {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username")
    private String username;

    @Column(name="first_name")
    private String firstname;

    @Column(name="last_name")
    private String lastname;

    @Column(name="mail")
    private String mail;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                                                   CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "word_users_data",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Collection<Word> wordCollection;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                                                   CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "grammar_users_data",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_id"))
    private Collection<Grammar> grammarCollection;

    public UsersData() {}

    public UsersData(String username, String firstname, String lastname, String mail) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
    }

    public UsersData(String username, String firstname, String lastname, String mail,
                     Collection<Word> wordCollection, Collection<Grammar> grammarCollection) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.mail = mail;
        this.wordCollection = wordCollection;
        this.grammarCollection = grammarCollection;
    }

    public void addWordCollection(Word word) {
        if (wordCollection == null)
            wordCollection = new ArrayList<>();
        wordCollection.add(word);
    }

    public void addGrammarCollection(Grammar grammar) {
        if (grammarCollection == null)
            grammarCollection = new ArrayList<>();
        grammarCollection.add(grammar);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Collection<Word> getWordCollection() {
        return wordCollection;
    }

    public void setWordCollection(Collection<Word> wordCollection) {
        this.wordCollection = wordCollection;
    }

    public Collection<Grammar> getGrammarCollection() {
        return grammarCollection;
    }

    public void setGrammarCollection(Collection<Grammar> grammarCollection) {
        this.grammarCollection = grammarCollection;
    }
}

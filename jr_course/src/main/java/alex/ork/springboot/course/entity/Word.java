package alex.ork.springboot.course.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="word")
public class Word {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="level")
	private String level;

	@Column(name="jp_kanji")
	private String jpKanji;

	@Column(name="jp_kana")
	private String jpKana;

	@Column(name="ru_word")
	private String ruWord;

	@Column(name="description")
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
												   CascadeType.DETACH, CascadeType.REFRESH})
	@JoinTable(name = "word_users_data",
			joinColumns = @JoinColumn(name = "word_id"),
			inverseJoinColumns = @JoinColumn(name = "users_id"))
	private List<UsersData> usersDataCollection;


	public Word() {}

	public Word(String level, String jpKanji, String jpKana, String ruWord, String description) {
		this.level = level;
		this.jpKanji = jpKanji;
		this.jpKana = jpKana;
		this.ruWord = ruWord;
		this.description = description;
	}

	public Word(String level, String jpKanji, String jpKana, String ruWord, String description,
				List<UsersData> usersDataCollection) {
		this.level = level;
		this.jpKanji = jpKanji;
		this.jpKana = jpKana;
		this.ruWord = ruWord;
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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getJpKanji() {
		return jpKanji;
	}

	public void setJpKanji(String jpKanji) {
		this.jpKanji = jpKanji;
	}

	public String getJpKana() {
		return jpKana;
	}

	public void setJpKana(String jpKana) {
		this.jpKana = jpKana;
	}

	public String getRuWord() {
		return ruWord;
	}

	public void setRuWord(String ruWord) {
		this.ruWord = ruWord;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UsersData> getUsersDataCollection() {
		return usersDataCollection;
	}

	public void setUsersDataCollection(List<UsersData> usersDataCollection) {
		this.usersDataCollection = usersDataCollection;
	}

	@Override
	public String toString() {
		return "Word{" +
				"id=" + id +
				", lvl='" + level + '\'' +
				", jpKanji='" + jpKanji + '\'' +
				", jpKana='" + jpKana + '\'' +
				", ruWord='" + ruWord + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}












# JR COURSE
### Japanese language learning project.
A project designed as a web site in Russian where you can learn Japanese,
built with Spring Boot. So far, implemented: dictionary section, grammar
section, user's personal account, administrator's personal account, user
authorization.<br>
In prospect, user registration will be available, additional internal
sorting with data, and more functionality will be implemented.

Using
---
1. Home page. Static information page.
2. Dictionary section, which has three difficulty levels.<br>
The user can add, change and delete a word. Each word can be written in
Japanese characters, Japanese kana (hiragana and katakana), in Russian,
as well as an additional description with examples of use. There is a word
search with the following logic:
    - The search is performed by hieroglyph, kana or Russian spelling (not
    only for the whole word, but also for its part).
    - In each section (level), the search is performed according to the
    level at which the user is.
    - In the general section of the dictionary, the search is carried out
    regardless of the level.
    - If the search word is not found, an empty list is returned to the
    user (and a button appears to view the entire list).
3. Section of grammar. Works in the same way as the dictionary section.
5 levels, according to the JLPT exam levels. There is sorting by levels,
search by part of a word.
4. JLPT Exam Information Section. Static information page.
5. Authorization of users with Spring Security with assignment of roles.
For the user with the **USER** role, another section is added - user's
personal account.
6. There are two sections in the user's personal account - a personal
dictionary and a personal set of grammar.<br>
   In the general dictionary, a user with the USER role has the opportunity
   to add each word to his dictionary, and remove it. The same
   functionality is implemented in the grammar. In personal materials, data
   can be deleted.
7. A user with the ADMIN role has access to a personal account, where he
can view complete information about all users. It is also possible to
delete a user one at a time, or all users except the administrator.


Technologies used in the project
---
- `Spring Boot`
- View: `HTML + Thymeleaf, CSS`
- Working with entities using `Spring Data JPA`.
- Authorization and data protection - `Spring Security`.
- For frontend development - `Bootstrap`.
- Logging is done using `Logback`.
---

###Note
Responsive and adaptive web design is not supported yet.
The connection to the database is done in the `application.properties` file.

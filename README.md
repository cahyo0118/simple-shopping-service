# Simple Shopping Service
Aplikasi ini dibangun menggunakan framework Spring Boot.

### System requirements
- Java 8
- Gradle 5
- PostgreSQL 11

### How to Run Application
- buat database di PostgreSQL
- buka file `src/main/resources/application-profile.properties`
- edit property `contextPath` dengan path folder untuk menyimpan data gambar dan file
- edit property `spring.datasource.url` dengan URL dan nama database yang telah di buat
- edit property `spring.datasource.username` dengan username database yang telah di buat
- edit property `spring.datasource.password` dengan password database yang telah di buat
- open file `src/main/resources/application.properties`
- Run : `mvn spring-boot:run`

### How To Use
- Anda perlu generate starter data, caranya dengan mengakses URL:`{{SERVER_URL}}api/seeders/starter-data` dengan metode `POST`

### Security
- Untuk mengakses URL yang dilindungi oleh security, anda perlu menambahkan Headers `Authorization: Bearer {token}`

### API Docs
https://documenter.getpostman.com/view/351640/TzK2ZZAR
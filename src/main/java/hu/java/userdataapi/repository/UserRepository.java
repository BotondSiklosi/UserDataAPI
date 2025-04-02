package hu.java.userdataapi.repository;

import hu.java.userdataapi.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findById(long id);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByName(String name);

    ArrayList<AppUser> findAll();

    void deleteById(long id);

    @Query("SELECT AVG(u.age) FROM AppUser u")
    Double findAverageAge();

    boolean existsByEmail(String email);

}

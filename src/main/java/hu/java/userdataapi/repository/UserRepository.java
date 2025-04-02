package hu.java.userdataapi.repository;

import hu.java.userdataapi.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findById(long id);

    ArrayList<User> findAll();

    void deleteById(long id);

    @Query("SELECT AVG(u.age) FROM User u")
    Double findAverageAge();

    boolean existsByEmail(String email);

}

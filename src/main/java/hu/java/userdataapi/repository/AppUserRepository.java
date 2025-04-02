package hu.java.userdataapi.repository;

import hu.java.userdataapi.entity.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByName(String name);

    List<AppUser> findAll();

    @Query("SELECT AVG(u.age) FROM AppUser u")
    Double findAverageAge();

    boolean existsByEmail(String email);
}

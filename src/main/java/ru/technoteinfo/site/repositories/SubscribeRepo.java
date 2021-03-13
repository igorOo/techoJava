package ru.technoteinfo.site.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.technoteinfo.site.entities.Subscribe;

import java.math.BigInteger;

public interface SubscribeRepo extends JpaRepository<Subscribe, BigInteger> {

}

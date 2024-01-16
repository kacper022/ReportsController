package pl.reportsController.Configs;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TechnicConfigRepository extends CrudRepository<TechnicsConfig, Long> {

    @Query("SELECT c FROM TechnicsConfig c WHERE c.id_technic = ?1")
    public TechnicsConfig getTechnicConfig(long id_technic);
}

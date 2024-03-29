package GDSCKNU.CitySavior.repository.report;

import GDSCKNU.CitySavior.entity.report.Report;
import java.util.List;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findById(Long reportId);

    @Query(value = "SELECT * FROM Report r " +
            "WHERE ST_DWithin(r.location, :point, :radius, false) = true", nativeQuery = true)
    List<Report> findReportsWithinRadius(@Param("point") Point point,
                                         @Param("radius") int radius);

    int countReportByMemberEmail(String email);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.member.email = :email AND r.repaired_date IS NOT NULL")
    int countReportByMemberEmailAndRepaired_dateIsNotNull(@Param("email") String email);
}

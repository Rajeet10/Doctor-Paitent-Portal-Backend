package doctor_paitent_portal.Doctor.Paitent.Portal.repository;


import doctor_paitent_portal.Doctor.Paitent.Portal.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatientId(Long patientId);
    List<MedicalRecord> findByDoctorId(Long doctorId);
    List<MedicalRecord> findByPatientIdOrderByVisitDateDesc(Long patientId);
    List<MedicalRecord> findByDoctorIdOrderByVisitDateDesc(Long doctorId);
    List<MedicalRecord> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);
    List<MedicalRecord> findByPatientIdAndDoctorId(Long patientId, Long doctorId);
}

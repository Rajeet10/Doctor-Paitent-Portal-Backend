package doctor_paitent_portal.Doctor.Paitent.Portal.repository;


import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    List<Appointment> findByAppointmentDate(LocalDate date);
    List<Appointment> findByStatus(Appointment.AppointmentStatus status);
    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(Long patientId);
    List<Appointment> findByDoctorIdOrderByAppointmentDateAsc(Long doctorId);
}
package doctor_paitent_portal.Doctor.Paitent.Portal.service;

import doctor_paitent_portal.Doctor.Paitent.Portal.dto.AppointmentRequest;
import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Appointment;
import doctor_paitent_portal.Doctor.Paitent.Portal.entity.AvailableSlot;
import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Doctor;
import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Patient;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.AppointmentRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.AvailableSlotRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.DoctorRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.PatientRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AvailableSlotRepository slotRepository;

    @Override
    @Transactional
    public Appointment bookAppointment(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Check slot availability
        AvailableSlot slot = slotRepository.findByDoctorIdAndSlotDateAndSlotTime(
                        doctor.getId(), request.getAppointmentDate(), request.getAppointmentTime())
                .orElseThrow(() -> new RuntimeException("Requested slot not available"));

        if (!slot.getIsAvailable()) {
            throw new RuntimeException("Requested slot is already booked");
        }

        // create appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);

        // mark slot unavailable
        slot.setIsAvailable(false);
        slotRepository.save(slot);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorIdOrderByAppointmentDateAsc(doctorId);
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(Long id, Appointment.AppointmentStatus status) {
        Appointment ap = getAppointmentById(id);
        ap.setStatus(status);
        return appointmentRepository.save(ap);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long id) {
        Appointment ap = getAppointmentById(id);
        ap.setStatus(Appointment.AppointmentStatus.CANCELLED);

        // Free up the slot if exists
        slotRepository.findByDoctorIdAndSlotDateAndSlotTime(
                ap.getDoctor().getId(), ap.getAppointmentDate(), ap.getAppointmentTime()
        ).ifPresent(s -> {
            s.setIsAvailable(true);
            slotRepository.save(s);
        });

        appointmentRepository.save(ap);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
}

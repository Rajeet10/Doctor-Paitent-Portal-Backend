package doctor_paitent_portal.Doctor.Paitent.Portal.service;

import doctor_paitent_portal.Doctor.Paitent.Portal.entity.AvailableSlot;
import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Doctor;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.AvailableSlotRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.DoctorRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AvailableSlotRepository slotRepository;

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    @Override
    public List<Doctor> getDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyContainingIgnoreCase(specialty);
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        Doctor existing = getDoctorById(id);
        existing.setName(doctor.getName());
        existing.setPhone(doctor.getPhone());
        existing.setQualification(doctor.getQualification());
        existing.setSpecialty(doctor.getSpecialty());
        existing.setExperienceYears(doctor.getExperienceYears());
        existing.setAbout(doctor.getAbout());
        existing.setConsultationFee(doctor.getConsultationFee());
        existing.setLicenseNumber(doctor.getLicenseNumber());
        return doctorRepository.save(existing);
    }

    @Override
    public AvailableSlot addSlot(AvailableSlot slot) {
        // slot.doctor should be set by controller before calling this method (controller already does that)
        return slotRepository.save(slot);
    }

    @Override
    public List<AvailableSlot> getDoctorSlots(Long id) {
        return slotRepository.findByDoctorId(id);
    }

    @Override
    public List<AvailableSlot> getAvailableSlots(Long id) {
        return slotRepository.findByDoctorIdAndIsAvailableTrue(id);
    }
}

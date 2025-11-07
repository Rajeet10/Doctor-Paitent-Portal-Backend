package doctor_paitent_portal.Doctor.Paitent.Portal.service;


import doctor_paitent_portal.Doctor.Paitent.Portal.entity.Patient;
import doctor_paitent_portal.Doctor.Paitent.Portal.repository.PatientRepository;
import doctor_paitent_portal.Doctor.Paitent.Portal.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {
        Patient existing = getPatientById(id);
        existing.setName(patient.getName());
        existing.setPhone(patient.getPhone());
        existing.setAddress(patient.getAddress());
        existing.setEmergencyContact(patient.getEmergencyContact());
        existing.setMedicalHistory(patient.getMedicalHistory());
        existing.setBloodGroup(patient.getBloodGroup());
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setGender(patient.getGender());
        return patientRepository.save(existing);
    }

    @Override
    public Long getActivePatientCount() {
        return patientRepository.countByActiveTrue();
    }
}

package tn.esprit.contractmanegement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.contractmanegement.Entity.Appointement;
import tn.esprit.contractmanegement.Repository.AppointementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointementService implements IAppointementService {

    private final AppointementRepository appointementRepository;

    @Autowired
    public AppointementService(AppointementRepository appointementRepository) {
        this.appointementRepository = appointementRepository;
    }

    // ✅ Create an appointment
    @Override
    public Appointement createAppointment(Appointement appointement) {
        return appointementRepository.save(appointement);
    }

    // ✅ Get all appointments
    @Override
    public List<Appointement> getAllAppointments() {
        List<Appointement> appointments = appointementRepository.findAll();
        return appointments.isEmpty() ? new ArrayList<>() : appointments;  // ✅ Always return a valid array
    }

    // ✅ Get an appointment by ID
    @Override
    public Optional<Appointement> getAppointmentById(Long id) {
        return appointementRepository.findById(id);
    }

    // ✅ Update appointment
    @Override
    public Appointement updateAppointment(Long appointementId, Appointement updatedAppointement) {
        return appointementRepository.findById(appointementId).map(existingAppointement -> {
            existingAppointement.setDescription(updatedAppointement.getDescription());
            existingAppointement.setDateSubmitted(updatedAppointement.getDateSubmitted());
            existingAppointement.setStatus(updatedAppointement.getStatus());
            return appointementRepository.save(existingAppointement);
        }).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // ✅ Delete appointment
    @Override
    public void deleteAppointment(Long id) {
        if (!appointementRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found");
        }
        appointementRepository.deleteById(id);
    }
}

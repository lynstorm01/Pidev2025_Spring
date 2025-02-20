package tn.esprit.contractmanegement.Service;

import tn.esprit.contractmanegement.Entity.Appointement;

import java.util.List;
import java.util.Optional;

public interface IAppointementService {

    // ✅ Create an appointment
    Appointement createAppointment(Appointement appointement);

    // ✅ Get all appointments
    List<Appointement> getAllAppointments();

    // ✅ Get an appointment by ID
    Optional<Appointement> getAppointmentById(Long id);

    // ✅ Update an appointment
    Appointement updateAppointment(Long appointementId, Appointement updatedAppointement);

    // ✅ Delete an appointment
    void deleteAppointment(Long id);
}

package tn.esprit.contractmanegement.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.contractmanegement.Entity.Appointement;
import tn.esprit.contractmanegement.Service.AppointementService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointementController {

    private final AppointementService appointementService;

    public AppointementController(AppointementService appointementService) {
        this.appointementService = appointementService;
    }

    // ✅ Create an appointment
    @PostMapping
    public ResponseEntity<Appointement> createAppointment(@Valid @RequestBody Appointement appointement) {
        try {
            Appointement createdAppointement = appointementService.createAppointment(appointement);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointement);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ✅ Get all appointments
    @GetMapping
    public ResponseEntity<List<Appointement>> getAllAppointments() {
        List<Appointement> appointments = appointementService.getAllAppointments();
        return appointments.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(appointments);
    }

    // ✅ Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointement> getAppointmentById(@PathVariable Long id) {
        Optional<Appointement> appointementOptional = appointementService.getAppointmentById(id);
        return appointementOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ✅ Update appointment
    @PutMapping("/{appointementId}")
    public ResponseEntity<Appointement> updateAppointment(@PathVariable Long appointementId, @Valid @RequestBody Appointement updatedAppointement) {
        Optional<Appointement> existingAppointement = appointementService.getAppointmentById(appointementId);
        if (existingAppointement.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Appointement appointement = appointementService.updateAppointment(appointementId, updatedAppointement);
        return ResponseEntity.ok(appointement);
    }

    // ✅ Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (appointementService.getAppointmentById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        appointementService.deleteAppointment(id);
        return ResponseEntity.noContent().build(); // ✅ Ensures correct return type
    }
}

package co.edu.gimnasiolorismalaguzzi.academyservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "table_name", nullable = false, length = 20)
    private String tableName;

    @Size(max = 20)
    @NotNull
    @Column(name = "operation", nullable = false, length = 20)
    private String operation;

    @NotNull
    @Column(name = "record_id", nullable = false)
    private Integer recordId;

    @Column(name = "changed_at")
    private OffsetDateTime changedAt;

    @NotNull
    @Column(name = "changed_by", nullable = false)
    private Integer changedBy;

    @Size(max = 20)
    @NotNull
    @Column(name = "ip", nullable = false, length = 20)
    private String ip;

}
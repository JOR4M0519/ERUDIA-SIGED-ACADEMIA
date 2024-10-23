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
@Table(name = "backup_history")
public class BackupHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "backup_date", nullable = false)
    private OffsetDateTime backupDate;

    @Size(max = 255)
    @NotNull
    @Column(name = "backup_name", nullable = false)
    private String backupName;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @NotNull
    @Column(name = "file_path", nullable = false, length = Integer.MAX_VALUE)
    private String filePath;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

}
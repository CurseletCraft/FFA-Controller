package mino.dx.ffacontroller.models;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportModel(int id, UUID uuidReporter, UUID uuidMember, String reason, ReportStatus status,
                          LocalDateTime lastAdded, LocalDateTime lastProcessed) {
}

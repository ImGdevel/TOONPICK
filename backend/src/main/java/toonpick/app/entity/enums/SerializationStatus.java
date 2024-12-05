package toonpick.app.entity.enums;

public enum SerializationStatus {
    ONGOING,
    PAUSED,
    COMPLETED;

    public static SerializationStatus fromKorean(String korean) {
        switch (korean) {
            case "연재":
                return ONGOING;
            case "휴재":
                return PAUSED;
            case "완결":
                return COMPLETED;
            default:
                throw new IllegalArgumentException("Unknown Korean status: " + korean);
        }
    }

    public String toKorean() {
        switch (this) {
            case ONGOING:
                return "연재";
            case PAUSED:
                return "휴재";
            case COMPLETED:
                return "완결";
            default:
                throw new IllegalStateException("Unknown status: " + this);
        }
    }
}

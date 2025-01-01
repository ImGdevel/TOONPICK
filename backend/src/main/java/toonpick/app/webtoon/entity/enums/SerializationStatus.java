package toonpick.app.webtoon.entity.enums;

public enum SerializationStatus {
    ONGOING,
    PAUSED,
    COMPLETED;

    public static SerializationStatus fromKorean(String korean) {
        if (korean == null || korean.isBlank()) {
            throw new IllegalArgumentException("Korean status cannot be null or blank");
        }

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

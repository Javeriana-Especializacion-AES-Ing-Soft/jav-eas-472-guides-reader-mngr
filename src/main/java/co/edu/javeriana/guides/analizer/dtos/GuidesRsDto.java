package co.edu.javeriana.guides.analizer.dtos;

import java.util.UUID;

public class GuidesRsDto {

    private UUID uuid;
    private GuideInfoDto guideInfo;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public GuideInfoDto getGuideInfo() {
        return guideInfo;
    }

    public void setGuideInfo(GuideInfoDto guideInfo) {
        this.guideInfo = guideInfo;
    }

}

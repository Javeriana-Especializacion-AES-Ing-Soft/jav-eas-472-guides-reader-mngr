package co.edu.javeriana.guides.analizer.dtos;


import co.edu.javeriana.guides.analizer.utilities.DtoUtilityTest;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import java.lang.reflect.InvocationTargetException;

class DtosTest {

    @Test
    void basicInfoDtoTest() throws InvocationTargetException, DatatypeConfigurationException, InstantiationException, IllegalAccessException {
        DtoUtilityTest.testDto(BasicInfoDto.class);
    }

    @Test
    void guideInfoDtoTest() throws InvocationTargetException, DatatypeConfigurationException, InstantiationException, IllegalAccessException {
        GuideInfoDto guideInfoDto = new GuideInfoDto();
        guideInfoDto.setSender(new SenderDto());
        guideInfoDto.setReceiver(new ReceiverDto());
        DtoUtilityTest.testDto(GuideInfoDto.class);
    }

    @Test
    void guidesRsDtoTest() throws InvocationTargetException, DatatypeConfigurationException, InstantiationException, IllegalAccessException {
        GuidesRsDto guidesRsDto = new GuidesRsDto();
        guidesRsDto.setGuideInfo(new GuideInfoDto());
        DtoUtilityTest.testDto(GuidesRsDto.class);
    }

    @Test
    void receiverDtoTest() throws InvocationTargetException, DatatypeConfigurationException, InstantiationException, IllegalAccessException {
        DtoUtilityTest.testDto(ReceiverDto.class);
    }

    @Test
    void senderDtoTest() throws InvocationTargetException, DatatypeConfigurationException, InstantiationException, IllegalAccessException {
        DtoUtilityTest.testDto(SenderDto.class);
    }

}

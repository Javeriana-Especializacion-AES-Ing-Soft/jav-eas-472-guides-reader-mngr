package co.edu.javeriana.guides.analizer.dtos;

public class GuideInfoDto {

    private boolean isComplete;
    private SenderDto sender = new SenderDto();
    private ReceiverDto receiver = new ReceiverDto();

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public SenderDto getSender() {
        return sender;
    }

    public void setSender(SenderDto sender) {
        this.sender = sender;
    }

    public ReceiverDto getReceiver() {
        return receiver;
    }

    public void setReceiver(ReceiverDto receiver) {
        this.receiver = receiver;
    }

}

import java.time.LocalTime;

public abstract class Clock {
    protected int hours;
    protected int min;
    protected int sec;
    private City city;

    public Clock(City city) {
        this.city = city;
    }

    public void setCity(City city) {
        if(city.getTimezone() != this.city.getTimezone()) {
            hours += city.getTimezone() - this.city.getTimezone();
            hours = (hours + 24) % 24;
        }
        this.city = city;
    }

    public void setCurrentTime(){
        LocalTime currentTime = LocalTime.now();
        this.hours = currentTime.getHour();
        this.min = currentTime.getMinute();
        this.sec = currentTime.getSecond();
    }

    public void setTime(int hours, int min, int sec){
        if(hours >= 24 || hours <= -1){
            throw new IllegalArgumentException("Godzina nie poprawna");
        }
        if(min >= 60 || min <= -1){
            throw new IllegalArgumentException("Minuty nie poprawne");
        }
        if(sec >= 60 || sec <= -1){
            throw new IllegalArgumentException("Secundy nie poprawne");
        }
        this.hours = hours;
        this.min = min;
        this.sec = sec;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hours, min, sec);
    }
}

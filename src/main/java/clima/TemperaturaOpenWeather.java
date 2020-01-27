package clima;

public class TemperaturaOpenWeather extends Clima {
	double temp;
    double pressure;
    double humidity;
    double temp_min;
    double temp_max;
	
    public TemperaturaOpenWeather(double temp,double pressure,double humidity,double temp_min,double temp_max) {
    	this.temp=temp;
    	this.pressure=pressure;
    	this.humidity=humidity;
    	this.temp_min=temp_min;
    	this.temp_max=temp_max;
    }
    
	@Override
	public double getTemperature() {
	
		return this.temp-273.15;
	}

	@Override
	public void setTemperature(double valor){
    	this.temp = valor + 273.15;
    }

}

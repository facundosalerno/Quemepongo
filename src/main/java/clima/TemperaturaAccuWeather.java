package clima;

public class TemperaturaAccuWeather extends Clima{

	double Value;
	String Unit;
	String UnitType;
	
	public TemperaturaAccuWeather (double Value,String Unit,String UnitType) {
		this.Value=Value;
		this.Unit=Unit;
		this.UnitType=UnitType;
		
	}
	
	public double getValue() {
		return Value;
	}
	public void setValue(float value) { Value = value; }
	public String getUnit() {
		return Unit;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public String getUnitType() {
		return UnitType;
	}
	public void setUnitType(String unitType) {
		UnitType = unitType;
	}
	
	@Override
	public double getTemperature() {
		
		return this.getValue();
	}

	@Override
	public void setTemperature(double valor){
		this.Value = valor;
	}
}


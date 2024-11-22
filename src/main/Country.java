package main;

public class Country {
	private String country;
	private String population;
	private String density;
	private String area;
	private String fertility;
	private String age;
	private String urban;
	private String share;

	public Country(String country, String population, String density, String area, String fertility, String age,
			String urban, String share) {
		super();
		this.country = country;
		this.population = population;
		this.density = density;
		this.area = area;
		this.fertility = fertility;
		this.age = age;
		this.urban = urban;
		this.share = share;
	}

	public Country(String[] info) {
		super();
		this.country = info[0];
		this.population = info[1];
		this.density = info[2];
		this.area = info[3];
		this.fertility = info[4];
		this.age = info[5];
		this.urban = info[6];
		this.share = info[7];
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPopulation() {
		return population;
	}

	public void setPopulation(String population) {
		this.population = population;
	}

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getFertility() {
		return fertility;
	}

	public void setFertility(String fertility) {
		this.fertility = fertility;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getUrban() {
		return urban;
	}

	public void setUrban(String urban) {
		this.urban = urban;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	@Override
	public String toString() {
		return "Country [country=" + country + ", population=" + population + ", density=" + density + ", area=" + area
				+ ", fertility=" + fertility + ", age=" + age + ", urban=" + urban + ", share=" + share + "]";
	}
}

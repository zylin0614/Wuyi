package datebase;

public class Works {
	String work_status;
	int work_id;
	String work_name;
	String work_description;
	String work_image;
	String work_price;
	public int getId() {
		return work_id;
	}

	public void setId(int id) {
		this.work_id = id;
	}

	public String getWork_status() {
		return work_status;
	}

	public void setWork_status(String work_status) {
		this.work_status = work_status;
	}

	public String getWork_name() {
		return work_name;
	}

	public void setWork_name(String work_name) {
		this.work_name = work_name;
	}

	public String getWork_description() {
		return work_description;
	}

	public void setWork_description(String work_description) {
		this.work_description = work_description;
	}

	public String getWork_image() {
		return work_image;
	}

	public void setWork_image(String work_image) {
		this.work_image = work_image;
	}

	public String getWork_price() {
		return work_price;
	}

	public void setWork_price(String work_price) {
		this.work_price = work_price;
	}

	public Works(int work_id,String work_status, String work_name, String work_description, String work_image, String work_price) {
		super();
		this.work_id = work_id;
		this.work_status = work_status;
		this.work_name = work_name;
		this.work_description = work_description;
		this.work_image = work_image;
		this.work_price = work_price;
	}
	public Works(String work_status, String work_name, String work_description, String work_image, String work_price) {
		super();
		this.work_status = work_status;
		this.work_name = work_name;
		this.work_description = work_description;
		this.work_image = work_image;
		this.work_price = work_price;
	}
}

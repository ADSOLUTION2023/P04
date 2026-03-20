package in.co.rays.proj4.bean;

import java.util.Date;

public class ShoppingBean extends BaseBean {

	    private long id;
	    private String productName;
	    private String shopName;
	    private String productPrice;
	    private Date purchaseDate;

	    public long getId() {
	        return id;
	    }

	    public void setId(long id) {
	        this.id = id;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public String getShopName() {
	        return shopName;
	    }

	    public void setShopName(String shopName) {
	        this.shopName = shopName;
	    }

	    public String getProductPrice() {
	        return productPrice;
	    }

	    public void setProductPrice(String productPrice) {
	        this.productPrice = productPrice;
	    }

	    public Date getPurchaseDate() {
	        return purchaseDate;
	    }

	    public void setPurchaseDate(Date purchaseDate) {
	        this.purchaseDate = purchaseDate;
	    }

		@Override
		public String getKey() {
			 
			return id + "";
		}

		@Override
		public String getValue() {
	 
			return productName ;
		}
	}

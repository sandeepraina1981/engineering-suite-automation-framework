package resources;

public enum APIResources {

    addProductAPI("api/ecom/product/add-product"),
    createOrderAPI("/api/ecom/order/create-order"),
    deleteProductAPI("/api/ecom/product/delete-product"),
    organization("/organizations");

    private final String resource;

    APIResources(String resource)
    {
        this.resource= resource;
    }

    public String getResource()
    {
        return resource;
    }


}

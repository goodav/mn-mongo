db = new Mongo().getDB("example");

db.createCollection('orders', { capped: false });

db.orders.insert([
    {
        "orderId":1,
        "firstName":"Dave",
        "lastName":"Goodin",
        "createdDate":  new Date(),
        "items":[
            {
                "itemId": 1,
                "description": "undies",
                "qty": 5
            }
        ]
    }
]);
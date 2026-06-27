import { useCart } from "../context/CartContext";

export default function Cart() {
  const { cart, loading, changeQuantity, removeItem, cartTotal } = useCart();

  if (loading) {
    return <p className="text-center mt-10">Loading cart...</p>;
  }

  if (!cart || cart.products.length === 0) {
    return <p className="text-center mt-10">Your cart is empty</p>;
  }

  return (
    <div className="max-w-5xl mx-auto grid grid-cols-1 lg:grid-cols-3 gap-6">

      {/* Cart Items */}
      <div className="lg:col-span-2 space-y-4">

        {cart.products.map((item) => (
          <div
            key={item.productId}
            className="bg-white p-4 shadow rounded flex items-center gap-4"
          >

            {/* Image */}
            <img
              src={item.image}
              className="w-20 h-20 object-cover rounded"
            />

            {/* Info */}
            <div className="flex-1">
              <h3 className="font-semibold">{item.productName}</h3>
              <div className="mt-3 space-y-1 text-sm">
                <p className="text-gray-700">
                  <span className="font-medium">Product Price:</span> 
                  {(item.specialPrice != null && item.specialPrice !== item.price) ? (
                  <div>
                      <span className="line-through text-gray-500 mr-2">
                          ${Number(item.price).toFixed(2)}
                      </span>
                          ${Number(item.specialPrice).toFixed(2)}
                  </div>
                  ) : (
                      <span>${Number(item.price).toFixed(2)}</span>
                  )}
                </p>
                {(item.discount != null && item.discount !== 0) && (
                  <p className="text-orange-500">
                    <span className="font-medium">Discount:</span> {Number(item.discount).toFixed(2)}%
                  </p>
                )}
              </div>

              <p>Total: ${(item.quantity * (item.specialPrice || item.price)).toFixed(2)}</p>

              {/* Quantity Controls */}
              <div className="flex items-center gap-2 mt-2">

                <button
                  onClick={() =>
                    changeQuantity(item.productId, "subtract")
                  }
                  className="px-2 bg-gray-200 rounded"
                >
                  -
                </button>

                <span>{item.quantity}</span>

                <button
                  onClick={() =>
                    changeQuantity(item.productId, "add")
                  }
                  className="px-2 bg-gray-200 rounded"
                >
                  +
                </button>

              </div>
            </div>

            {/* Remove */}
            <button
              onClick={() =>
                removeItem(cart.cartId, item.productId)
              }
              className="text-red-500"
            >
              Remove
            </button>

          </div>
        ))}

      </div>

      {/* Summary */}
      <div className="bg-white p-4 shadow rounded h-fit">

        <h2 className="text-xl font-bold mb-4">Order Summary</h2>

        <p className="flex justify-between">
          <span>Total:</span>
          <span>${cartTotal.toFixed(2)}</span>
        </p>

        <button className="w-full mt-4 bg-blue-600 text-white py-2 rounded">
          Checkout
        </button>

      </div>

    </div>
  );
}
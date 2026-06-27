import { Link } from "react-router-dom";
import { useCart } from "../../context/CartContext";


export default function ProductCard({ product }) {
  const productId = product?.productId ?? product?.id;
  const productName = product?.productName ?? "Unnamed Product";
  const description = product?.description ?? "No description available.";
  const price = product?.price ?? 0;
  const specialPrice = product?.specialPrice;
  const discount = product?.discount;
  const quantity = Number(product?.quantity ?? 0);
  const isOutOfStock = quantity <= 0;
  const imageSrc = product?.image && product.image.trim() !== ""
    ? product.image
    : "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300' viewBox='0 0 400 300'%3E%3Crect width='400' height='300' fill='%23f3f4f6'/%3E%3Ctext x='50%25' y='50%25' dominant-baseline='middle' text-anchor='middle' font-size='24' fill='%236b7280'%3ENo Image Available%3C/text%3E%3C/svg%3E";

  const { addToCart } = useCart();

  return (
    <div className="bg-white shadow rounded-lg p-4 hover:shadow-lg transition flex flex-col h-full">
      <img
        src={imageSrc}
        alt={productName}
        className="h-40 w-full object-cover rounded"
      />

      <div className="mt-3 flex-1">
        <h3 className="font-semibold text-lg">{productName}</h3>
        <p className="text-sm text-gray-600 mt-2 line-clamp-3">{description}</p>

        <div className="mt-3 space-y-1 text-sm">
          <p className="text-gray-700">
            <span className="font-medium">Price:</span> 
            {(specialPrice != null && specialPrice !== price) ? (
            <div>
                <span className="line-through text-gray-500 mr-2">
                    ${Number(price).toFixed(2)}
                </span>
                    ${Number(specialPrice).toFixed(2)}
            </div>
            ) : (
                <span>${Number(price).toFixed(2)}</span>
            )}
          </p>
          {(discount != null && discount !== 0) && (
            <p className="text-orange-500">
              <span className="font-medium">Discount:</span> {Number(discount).toFixed(2)}%
            </p>
          )}
          <p className={`font-medium ${isOutOfStock ? "text-red-500" : "text-gray-700"}`}>
            Stock: {quantity}
          </p>
        </div>
      </div>

      <div className="mt-4 flex flex-col gap-2">
        {isOutOfStock ? (
          <button
            type="button"
            disabled
            className="w-full rounded bg-gray-300 px-3 py-2 text-sm font-medium text-gray-700 cursor-not-allowed"
          >
            Out of Stock
          </button>
        ) : (
          <button
            type="button"
            className="w-full rounded bg-blue-600 px-3 py-2 text-sm font-medium text-white hover:bg-blue-700"
            onClick={() => addToCart(productId)}
          >
            Add to Cart
          </button>
        )}

        <Link
          to={`/product/${productId}`}
          state={{ product }}
          className="text-center text-blue-500 text-sm font-medium"
        >
          View Details
        </Link>
      </div>
    </div>
  );
}
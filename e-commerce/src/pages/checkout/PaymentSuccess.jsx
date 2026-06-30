import { useNavigate } from "react-router-dom";
import { FaCheckCircle } from "react-icons/fa";

export default function PaymentSuccess() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-green-50 via-white to-emerald-100 px-4">

      <div className="w-full max-w-lg bg-white rounded-2xl shadow-xl p-8 text-center">

        {/* Icon */}
        <div className="flex justify-center">
          <FaCheckCircle className="text-green-500 text-7xl" />
        </div>

        {/* Title */}
        <h1 className="mt-6 text-3xl font-bold text-gray-800">
          Payment Successful 🎉
        </h1>

        {/* Subtitle */}
        <p className="mt-3 text-gray-600">
          Your order has been placed successfully.
          We’re preparing it for shipment 🚀
        </p>

        {/* Success Box */}
        <div className="mt-6 bg-green-50 border border-green-200 rounded-xl p-4 text-left">
          <p className="text-green-700 font-semibold">
            ✔ Order Confirmed
          </p>

          <p className="text-sm text-green-600 mt-1">
            You will receive a confirmation email shortly with order details.
          </p>
        </div>

        {/* Actions */}
        <div className="mt-8 flex flex-col sm:flex-row gap-3">

          <button
            onClick={() => navigate("/")}
            className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 rounded-lg transition"
          >
            Continue Shopping
          </button>

        </div>

      </div>

    </div>
  );
}
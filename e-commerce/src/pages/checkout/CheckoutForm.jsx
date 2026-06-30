import { PaymentElement } from "@stripe/react-stripe-js";
import { useStripe, useElements } from "@stripe/react-stripe-js";


export default function CheckoutForm() {

    const stripe = useStripe();

    const elements = useElements();

    async function handleSubmit(event) {

        event.preventDefault();

        if (!stripe || !elements) {
            return;
        }
        const result = await stripe.confirmPayment({
            elements,
            confirmParams: {
                return_url:
                    "http://localhost:5173/payment-success",
            },
        });
        if (result.error) {
            alert(result.error.message);
        } else {
            alert("Payment successful!");

        }

    }

    return (

        <form onSubmit={handleSubmit}>

            <PaymentElement />

            <button 
                className="w-full mt-4 bg-blue-600 text-white py-2 rounded"
                type="submit"
                disabled={!stripe}
            >
                Pay Now
            </button>

        </form>

    );

}
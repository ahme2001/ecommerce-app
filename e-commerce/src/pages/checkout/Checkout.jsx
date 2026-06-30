import { useEffect, useState, useRef } from "react";
import { Elements } from "@stripe/react-stripe-js";
import { loadStripe } from "@stripe/stripe-js";

import CheckoutForm from "./CheckoutForm";
import { createPaymentIntent } from "../../api/payment-api";
import { useCart } from "../../context/CartContext";

const stripePromise = loadStripe(
    import.meta.env.VITE_STRIPE_PUBLIC_KEY
);

export default function Checkout() {

    const [clientSecret, setClientSecret] = useState();
    const { cart, addressId } = useCart();
    const initialized = useRef(false)

    // Replace this later with your real cart id
    const cartId = cart?.cartId || 1;

    useEffect(() => {

        if(initialized.current) {
            return;
        }

        initialized.current = true

        async function initializePayment() {
            await createPaymentIntent(cartId,addressId)
                .then( (res) => 
                    setClientSecret(res.data.clientSecretKey)
                ).catch( (err) => {
                    console.log(err)
        });
        }
        
        initializePayment();
    }, []);

    if (!clientSecret) {

        return <h2>Loading payment...</h2>;

    }

    return (

        <Elements
            stripe={stripePromise}
            options={{
                clientSecret,
            }}
        >

            <CheckoutForm />

        </Elements>

    );

}
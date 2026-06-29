import {BrowserRouter as Router, Routes, Route} from "react-router-dom";

import ProtectedRoute from "./ProtectedRoute";
import Products from "../pages/Products";
import Cart from "../pages/Cart";
import Auth from "../pages/Auth";
import ProductDetails from "../pages/ProductDetails";
import Checkout from "../pages/checkout/Checkout";
import AppLayout from "../components/layout/AppLayout";
import PaymentSuccess from "../pages/checkout/PaymentSuccess";
import PaymentCancel from "../pages/checkout/PaymentCancel";
import { CartProvider } from '../context/CartContext';

const AppRouter = () => {
    return (
        <Router>
            <Routes>
                    <Route element={
                        <CartProvider>
                            <AppLayout />
                        </CartProvider>
                        } >
                        <Route path="*" element={<Products />} />
                        <Route path="/cart" element={<ProtectedRoute><Cart /></ProtectedRoute>} />
                        <Route path="/product/:id" element={<ProtectedRoute><ProductDetails /></ProtectedRoute>} />
                        <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
                        <Route path="/payment-success" element={<ProtectedRoute><PaymentSuccess /></ProtectedRoute>} />
                        <Route path="/payment-cancel" element={<ProtectedRoute><PaymentCancel /></ProtectedRoute>} />

                    </Route>

                <Route path="/auth" element={<Auth />} />

            </Routes>
        </Router>
    );
}

export default AppRouter;
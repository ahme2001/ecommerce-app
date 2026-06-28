import {BrowserRouter as Router, Routes, Route} from "react-router-dom";

import ProtectedRoute from "./ProtectedRoute";
import Products from "../pages/Products";
import Cart from "../pages/Cart";
import Auth from "../pages/Auth";
import ProductDetails from "../pages/ProductDetails";
import Checkout from "../pages/Checkout";
import AppLayout from "../components/layout/AppLayout";


const AppRouter = () => {
    return (
        <Router>
            <Routes>

                <Route element={<AppLayout />} >
                    <Route path="*" element={<Products />} />
                    <Route path="/cart" element={<ProtectedRoute><Cart /></ProtectedRoute>} />
                    <Route path="/product/:id" element={<ProtectedRoute><ProductDetails /></ProtectedRoute>} />
                    <Route path="/checkout" element={<ProtectedRoute><Checkout /></ProtectedRoute>} />
                </Route>


                <Route path="/auth" element={<Auth />} />

            </Routes>
        </Router>
    );
}

export default AppRouter;
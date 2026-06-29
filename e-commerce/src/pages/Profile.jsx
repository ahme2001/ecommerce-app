import { useAuth } from "../context/AuthContext";
import { createAddress , getAddresses , deleteAddressById } from "../api/address-api"
import { useEffect, useState } from "react";
import AddressCard from "../components/address/AddressCard";

export default function ProfileModal({ onClose }) {
  const { user } = useAuth();
  const [ address, setAddress] = useState([])
  const [ loading, setLoading] = useState(false)
  const [ error, setError] = useState()

  const [form, setForm] = useState({
    street: "",
    buildingName: "",
    city: "",
    state: "",
    country: "",
    postalCode: ""
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    
    try {
        const response = await createAddress(form);
        setAddress((prev) => [...prev, response])

        // reset form
        setForm({
          street: "",
          buildingName: "",
          city: "",
          state: "",
          country: "",
          postalCode: ""
        });
    } catch (err) {
      const message = err?.response?.data?.message;
      setError(message || "Can't create Address");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    try {
      await deleteAddressById(id);
      setAddress((prev) =>
        prev.filter((addr) => (addr.addressId ?? addr.id) !== id));
    }
    catch (err) {
      const message = err?.response?.data?.message;
      setError(message || "Can't delete Address");
    }
  } 



  useEffect( () => {
    const getAddressesForUser = async () => {
      try{
        const addressesData = await getAddresses();
        setAddress(addressesData || [])
      } catch (err) {
        console.error(err);
      }
    }

    getAddressesForUser()
  }, []);


  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">

      {/* Modal Box */}
      <div className="bg-white w-full max-w-3xl max-h-[90vh] overflow-y-auto rounded-xl shadow-lg p-6 relative">

        {/* Close Button */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-gray-500 hover:text-black text-xl"
        >
          ✕
        </button>

        {/* HEADER */}
        <div className="mb-6 border-b pb-4">
          <h2 className="text-2xl font-bold">Profile</h2>

          <p className="text-gray-600 mt-1">
            <span className="font-semibold">Username:</span> {user?.username}
          </p>

          <p className="text-gray-600">
            <span className="font-semibold">Email:</span> {user?.email}
          </p>
        </div>

        {/* ADDRESS LIST */}
        <div className="mb-8">
          <h3 className="text-lg font-semibold mb-3">
            Saved Addresses
          </h3>

          {address.length === 0 ? (
            <p className="text-gray-500">No addresses yet</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {address.map((addr) => (
                <div className="relative">
                  <AddressCard
                    key={addr.addressId || addr.id}
                    address={addr}
                  />
                  <div className="mt-2 flex justify-end gap-2">
                    <button  className="px-3 py-1 text-sm bg-red-100 text-red-600 hover:bg-red-200 rounded-md"
                      onClick={() => handleDelete(addr.addressId)}
                    >
                      Delete
                    </button>
                  </div>
              </div>
              ))}
            </div>
          )}
        </div>

        {/* ADD ADDRESS FORM */}
        <div className="border-t pt-6">
          <h3 className="text-lg font-semibold mb-3">
            Add New Address
          </h3>

          <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-3">

            <input
              className="border p-2 rounded"
              placeholder="Street"
              value={form.street}
              onChange={(e) =>
                setForm({ ...form, street: e.target.value })
              }
            />

            <input
              className="border p-2 rounded"
              placeholder="Building Name"
              value={form.buildingName}
              onChange={(e) =>
                setForm({ ...form, buildingName: e.target.value })
              }
            />

            <input
              className="border p-2 rounded"
              placeholder="City"
              value={form.city}
              onChange={(e) =>
                setForm({ ...form, city: e.target.value })
              }
            />

            <input
              className="border p-2 rounded"
              placeholder="State"
              value={form.state}
              onChange={(e) =>
                setForm({ ...form, state: e.target.value })
              }
            />

            <input
              className="border p-2 rounded"
              placeholder="Country"
              value={form.country}
              onChange={(e) =>
                setForm({ ...form, country: e.target.value })
              }
            />

            <input
              className="border p-2 rounded"
              placeholder="Postal Code"
              value={form.postalCode}
              onChange={(e) =>
                setForm({ ...form, postalCode: e.target.value })
              }
            />

            <button
              type="submit"
              disabled={loading}
              className="col-span-2 bg-blue-600 text-white py-2 rounded disabled:opacity-50"
            >
              {loading ? "Saving..." : "Add Address"}
            </button>

          </form>

          {error && (
            <p className="text-red-500 mt-2">{error}</p>
          )}

        </div>

      </div>
    </div>
  );
}
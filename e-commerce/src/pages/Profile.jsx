import { useAuth } from "../context/AuthContext";

export default function ProfileModal({ onClose }) {
  const { user } = useAuth();

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">

      <div className="bg-white p-6 rounded w-96 space-y-4">

        <div className="flex justify-between items-center">
          <h2 className="text-xl font-bold">Profile</h2>

          <button onClick={onClose}>✕</button>
        </div>

        <div className="space-y-2">
          <p><strong>Name:</strong> {user?.username}</p>
          <p><strong>Email:</strong> {user?.email}</p>
        </div>

        <button
          onClick={onClose}
          className="w-full bg-blue-600 text-white py-2 rounded"
        >
          Close
        </button>

      </div>

    </div>
  );
}


export default function AddressCard({ address: address }) {
  const addressId = address?.addressId ?? address?.id;
  const street = address?.street ?? "Unnamed street";
  const buildingName = address?.buildingName ?? "No buildingName available.";
  const city = address?.city ?? "NO City";
  const state = address?.state ?? "NO state";
  const country = address?.country ?? "NO country";
  const postalCode = address?.postalCode ?? "NO postalCode";
  
  return (
    <div className="bg-white border border-gray-200 rounded-xl shadow-sm hover:shadow-md transition p-5 w-full">

      {/* Header */}
      <div className="flex justify-between items-start mb-3">
        <h3 className="text-lg font-semibold text-gray-800">
          Address #{addressId}
        </h3>

        <span className="text-xs px-2 py-1 bg-blue-100 text-blue-600 rounded-full">
          Shipping
        </span>
      </div>

      {/* Address Details */}
      <div className="space-y-1 text-sm text-gray-600">

        <p className="font-medium text-gray-800">
          {street}
        </p>

        <p>{buildingName}</p>

        <p>
          {city}, {state}
        </p>

        <p>
          {country} - {postalCode}
        </p>

      </div>

    </div>
  );
}
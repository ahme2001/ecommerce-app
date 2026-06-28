export default function LoadingButton({ loading, children, ...props }) {
  return (
    <button
      disabled={loading}
      className="bg-blue-600 text-white px-4 py-2 rounded disabled:opacity-50"
      {...props}
    >
      {loading ? "Loading..." : children}
    </button>
  );
}
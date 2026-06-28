export default function ErrorBox({ message }) {
  return (
    <div className="bg-red-100 text-red-600 p-3 rounded">
      {message}
    </div>
  );
}
export default function Pagination({ page, totalPages, setPage }) {
  return (
    <div className="flex gap-2 mt-6 justify-center">
      
      <button
        disabled={page === 0}
        onClick={() => setPage(page - 1)}
        className="px-3 py-1 border rounded"
      >
        Prev
      </button>

      <span className="px-3 py-1">
        Page {page + 1} of {totalPages}
      </span>

      <button
        disabled={page + 1 >= totalPages}
        onClick={() => setPage(page + 1)}
        className="px-3 py-1 border rounded"
      >
        Next
      </button>

    </div>
  );
}
export default function Home() {
  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] px-4 py-12">
      {/* Google Logo Placeholder */}
      <div className="mb-6">
        <svg className="w-12 h-12" viewBox="0 0 24 24">
          <path
            fill="#4285F4"
            d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
          />
          <path
            fill="#34A853"
            d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
          />
          <path
            fill="#FBBC05"
            d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z"
          />
          <path
            fill="#EA4335"
            d="M12 5.38c1.62 0 3.06.56 4.21 1.66l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
          />
        </svg>
      </div>

      <h1 className="text-4xl text-gray-800 font-bold mb-10">Sign in</h1>

      <div className="w-full max-w-md space-y-4">
        {/* Input Fields */}
        <input
          type="email"
          placeholder="Email"
          className="w-full p-4 bg-gray-200 rounded text-center text-gray-800 outline-none focus:ring-2 focus:ring-blue-400"
        />
        <input
          type="password"
          placeholder="Password"
          className="w-full p-4 bg-gray-200 rounded text-center text-gray-800 outline-none focus:ring-2 focus:ring-blue-400"
        />

        {/* Buttons */}
        <div className="flex flex-col gap-3 pt-4">
          <button className="w-32 mx-auto py-2 bg-gray-200 rounded font-medium text-gray-800 hover:bg-gray-300 transition">
            Logga in
          </button>
          <button className="w-32 mx-auto py-2 bg-gray-200 rounded font-medium text-gray-800 hover:bg-gray-300 transition text-sm">
            Admin
          </button>
        </div>

        <div className="flex flex-col gap-3 pt-12">
          <button className="w-64 mx-auto py-3 bg-gray-200 rounded font-medium text-gray-800 hover:bg-gray-300 transition">
            Skapa konto
          </button>
          <button className="w-64 mx-auto py-3 bg-gray-200 rounded font-medium text-gray-800 hover:bg-gray-300 transition">
            Tillbaka
          </button>
        </div>
      </div>
    </div>
  );
}
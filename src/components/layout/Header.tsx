import Link from 'next/link';

const Header = () => {
  return (
    <header className="w-full border-b bg-gray-50 border-gray-200 font-sans">

      {/* Main Nav */}
      <div className="flex items-center justify-between px-8 py-4">
        <div className="flex items-center gap-12">
          {/* Burger Menu Placeholder */}
          <div className="flex flex-col gap-1.5 cursor-pointer group">
            <span className="w-8 h-0.5 bg-black"></span>
            <span className="w-8 h-0.5 bg-black"></span>
            <span className="w-8 h-0.5 bg-black"></span>
            <span className="text-[10px] font-bold mt-1 text-center text-gray-900">MENY</span>
          </div>

          {/* Logo Placeholder */}
          <div className="relative w-32 h-12   flex items-center justify-center">
             <Link href="/" className="font-bold text-center text-gray-900 leading-tight z-10 bg-gray-50 px-1">OMEGA<br/>BANK</Link>
          </div>

          {/* Navigation Links */}
          <nav className="hidden md:flex gap-6 text-sm font-medium">
            <Link className="text-gray-900" href="/loantemplates">Lån</Link>
            <Link className="text-gray-900" href="/creditcardtemplates">Kreditkort</Link>
            <Link className="text-gray-900" href="/mypages">Mina sidor</Link>
          </nav>
        </div>

        <div className="flex items-center gap-6">
          <Link href="/kundservice" className="text-sm text-gray-900 font-medium">Kundservice</Link>
          <button className="flex items-center text-gray-900 gap-2 bg-gray-200 px-4 py-2 rounded text-sm font-bold hover:bg-gray-300 transition">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
              <path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
            </svg>
            Logga in
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;
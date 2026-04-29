import Link from 'next/link';
import LoginButton from "@/components/layout/LoginButton";


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
            <div className="relative w-32 h-12 flex items-center justify-center">
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

            {/* Här har jag bytt ut din gamla knapp mot LoginButton-komponenten */}
            <LoginButton />
          </div>
        </div>

      </header>
  );
};

export default Header;
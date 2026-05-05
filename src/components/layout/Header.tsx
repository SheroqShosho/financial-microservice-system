"use client";

import Link from 'next/link';
import LoginButton from "@/components/layout/LoginButton";
import { useEffect, useState } from "react";

const Header = () => {
  const [scrolled, setScrolled] = useState(false);

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 20);
    window.addEventListener("scroll", onScroll, { passive: true });
    return () => window.removeEventListener("scroll", onScroll);
  }, []);

  return (
      <header
          className="w-full fixed top-0 left-0 z-50 font-sans transition-all duration-300"
          style={{
            background: scrolled ? "#003349" : "transparent",
            borderBottom: scrolled ? "1px solid rgba(255,255,255,0.08)" : "1px solid transparent",
            backdropFilter: scrolled ? "none" : "none",
          }}
      >
        <div className="flex items-center justify-between px-8 md:px-16 py-5">

          {/* Vänster: logo + nav */}
          <div className="flex items-center gap-10">

            {/* Logo */}
            <Link href="/" className="font-bold uppercase"
                  style={{ fontFamily: "'Georgia', serif", letterSpacing: "0.18em", fontSize: "1.05rem", color: "#ffffff", textDecoration: "none" }}>
              Omega<span style={{ color: "#a8cdd9" }}>Bank</span>
            </Link>

            {/* Nav */}
            <nav className="hidden md:flex gap-8">
              <Link href="/loantemplates"
                    style={{ fontFamily: "sans-serif", fontSize: "0.85rem", fontWeight: 500, color: "rgba(255,255,255,0.65)", textDecoration: "none" }}
                    className="hover:text-white">Lån</Link>
              <Link href="/creditcardtemplates"
                    style={{ fontFamily: "sans-serif", fontSize: "0.85rem", fontWeight: 500, color: "rgba(255,255,255,0.65)", textDecoration: "none" }}
                    className="hover:text-white">Kreditkort</Link>
              <Link href="/mypages"
                    style={{ fontFamily: "sans-serif", fontSize: "0.85rem", fontWeight: 500, color: "rgba(255,255,255,0.65)", textDecoration: "none" }}
                    className="hover:text-white">Mina sidor</Link>
            </nav>
          </div>

          {/* Höger */}
          <div className="flex items-center gap-6">
            <Link href="/kundservice"
                  style={{ fontFamily: "sans-serif", fontSize: "0.85rem", fontWeight: 500, color: "rgba(255,255,255,0.55)", textDecoration: "none" }}
                  className="hidden md:block hover:text-white">Kundservice</Link>
            <LoginButton />
          </div>

        </div>
      </header>
  );
};

export default Header;
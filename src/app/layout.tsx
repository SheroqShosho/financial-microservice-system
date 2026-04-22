import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
// Importera dina nya komponenter här
import Header from "@/components/layout/Header";
import Footer from "@/components/layout/Footer";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Ikano Bank Kopia", // Uppdatera titeln här
  description: "Ett praktikprojekt i Next.js",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="sv" // Ändra till 'sv' eftersom sidan är på svenska
      className={`${geistSans.variable} ${geistMono.variable} h-full antialiased`}
    >
      <body className="min-h-full flex flex-col">
        {/* Headern ligger högst upp på alla sidor */}
        <Header />
        
        {/* 'flex-grow' ser till att main-ytan tar upp all plats så footern hamnar längst ner */}
        <main className="flex-grow">
          {children}
          
        </main>

        {/* Footern ligger längst ner på alla sidor */}
        <Footer />
      </body>
    </html>
  );
}
import Link from "next/link";
import styles from "./page.module.css";

export default function Home() {
  return (
      <div style={{ fontFamily: "'Georgia', 'Times New Roman', serif", minHeight: "100vh" }}>

        {/* ── HERO ── */}
        <section className="relative flex items-end overflow-hidden" style={{ minHeight: "100svh" }}>
          <img
              src="/omegabank-frontpage.png"
              alt="OmegaBank"
              className="absolute inset-0 w-full h-full object-cover object-center"
              style={{ zIndex: 0 }}
          />
          <div className="absolute inset-0" style={{
            background: "linear-gradient(to bottom, rgba(0,51,73,0.08) 0%, rgba(0,51,73,0.52) 52%, rgba(0,51,73,0.96) 100%)",
            zIndex: 1,
          }} />


          <div className="relative w-full px-10 md:px-16 pb-16 md:pb-24" style={{ zIndex: 2 }}>
            <p className="text-xs uppercase mb-4" style={{ color: "#a8cdd9", letterSpacing: "0.22em" }}>
              Bolån utan förhandling
            </p>
            <h1 className="text-white font-bold leading-none mb-6"
                style={{ fontSize: "clamp(2.6rem, 7vw, 5.5rem)", maxWidth: "720px", lineHeight: 1.05, letterSpacing: "-0.02em" }}>
              Välj ett<br />förhandlings&shy;fritt bolån
            </h1>
            <p className="text-base md:text-lg mb-10 max-w-md leading-relaxed"
               style={{ color: "rgba(255,255,255,0.75)", fontFamily: "sans-serif", fontWeight: 300 }}>
              Räkna på vilken ränta vi kan erbjuda dig - och se din bästa ränta direkt.
            </p>

          </div>

          <div className="absolute bottom-8 right-10 md:right-16 hidden md:flex items-center gap-2" style={{ zIndex: 2 }}>
            <div style={{ width: 28, height: 1, background: "rgba(255,255,255,0.35)" }} />
            <span style={{ color: "rgba(255,255,255,0.4)", fontSize: "0.68rem", letterSpacing: "0.15em", fontFamily: "sans-serif", textTransform: "uppercase" }}>
            Sedan 1923
          </span>
          </div>
        </section>

        {/* ── CARDS ── */}
        <section style={{ background: "#0a3d52" }} className="px-8 md:px-16 py-16 md:py-20">
          <div className={styles.cardGrid}>

            <Link href="/loantemplates/mortgage" className={styles.card}>
              <svg viewBox="0 0 24 24" fill="none" className={styles.cardIcon}>
                <path d="M3 12L12 4L21 12V20C21 20.55 20.55 21 20 21H15V15H9V21H4C3.45 21 3 20.55 3 20V12Z"
                      stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round"/>
              </svg>
              <span className={styles.cardLabel}>Bolån</span>
            </Link>

            <Link href="/loantemplates/private" className={styles.card}>
              <svg viewBox="0 0 24 24" fill="none" className={styles.cardIcon}>
                <rect x="2" y="5" width="20" height="14" rx="2" stroke="currentColor" strokeWidth="1.6"/>
                <circle cx="8" cy="12" r="2.5" stroke="currentColor" strokeWidth="1.4"/>
                <path d="M13 9H19M13 12H17M13 15H15" stroke="currentColor" strokeWidth="1.3" strokeLinecap="round"/>
              </svg>
              <span className={styles.cardLabel}>Privatlån</span>
            </Link>

            <Link href="/creditcardtemplates" className={styles.card}>
              <svg viewBox="0 0 24 24" fill="none" className={styles.cardIcon}>
                <rect x="2" y="5" width="20" height="14" rx="2" stroke="currentColor" strokeWidth="1.6"/>
                <path d="M2 10H22" stroke="currentColor" strokeWidth="1.6"/>
                <rect x="5" y="14" width="5" height="2" rx="0.5" fill="currentColor"/>
                <rect x="12" y="14" width="3" height="2" rx="0.5" fill="currentColor"/>
              </svg>
              <span className={styles.cardLabel}>Kreditkort</span>
            </Link>

          </div>
        </section>

      </div>
  );
}
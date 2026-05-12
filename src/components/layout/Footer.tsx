"use client";

const Footer = () => {
  return (
      <footer className="w-full mt-auto"
              style={{ background: "#003349", borderTop: "1px solid rgba(255,255,255,0.08)" }}>

        <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-12 px-8 md:px-16 py-16">

          {/* Varumärke */}
          <div>
            <p className="font-bold uppercase mb-3"
               style={{ fontFamily: "'Georgia', serif", letterSpacing: "0.18em", fontSize: "1.05rem", color: "#ffffff" }}>
              Omega<span style={{ color: "#a8cdd9" }}>Bank</span>
            </p>
          </div>

          {/* Snabblänkar */}
          <div>
            <h3 className="font-semibold mb-4 uppercase"
                style={{ fontFamily: "sans-serif", fontSize: "0.7rem", letterSpacing: "0.18em", color: "rgba(168,205,217,0.6)" }}>
              Snabblänkar
            </h3>
            <ul style={{ listStyle: "none", padding: 0, margin: 0 }} className="space-y-3">
              {["Om oss", "Säkerhet", "Cookies"].map((item) => (
                  <li key={item}>
                    <a href="#"
                       style={{ fontFamily: "sans-serif", fontSize: "0.875rem", color: "rgba(255,255,255,0.55)", textDecoration: "none", transition: "color 0.15s" }}
                       onMouseEnter={e => (e.currentTarget.style.color = "rgba(255,255,255,0.9)")}
                       onMouseLeave={e => (e.currentTarget.style.color = "rgba(255,255,255,0.55)")}>
                      {item}
                    </a>
                  </li>
              ))}
            </ul>
          </div>

          {/* Kontakt */}
          <div>
            <h3 className="font-semibold mb-4 uppercase"
                style={{ fontFamily: "sans-serif", fontSize: "0.7rem", letterSpacing: "0.18em", color: "rgba(168,205,217,0.6)" }}>
              Kontakt
            </h3>
            <p style={{ fontFamily: "sans-serif", fontSize: "0.875rem", color: "rgba(255,255,255,0.55)", marginBottom: "0.5rem" }}>
              Tel: 0771-32 32 32
            </p>
            <p style={{ fontFamily: "sans-serif", fontSize: "0.8rem", color: "rgba(255,255,255,0.35)", fontStyle: "italic", lineHeight: 1.5 }}>
              Öppet dygnet runt för spärrning av kort.
            </p>
          </div>

        </div>

        {/* Nedre rad */}
        <div className="px-8 md:px-16 py-5" style={{ borderTop: "1px solid rgba(255,255,255,0.06)" }}>
          <p style={{ fontFamily: "sans-serif", fontSize: "0.7rem", color: "rgba(255,255,255,0.2)" }}>
            © {new Date().getFullYear()} OmegaBank AB · Org.nr 556123-4567 · Tillstånd från Finansinspektionen
          </p>
        </div>

      </footer>
  );
};

export default Footer;
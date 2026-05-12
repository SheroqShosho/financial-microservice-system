"use client";

import { useState } from "react";

const ContactMethodCard = ({ icon, title, description, details, color }: { icon: string; title: string; description: string; details: string[]; color: string }) => (
    <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100 hover:shadow-2xl transition-shadow">
        <div className={`inline-block p-4 rounded-2xl mb-6 ${color}`}>
            <span className="text-2xl">{icon}</span>
        </div>
        <h3 className="text-lg font-black uppercase tracking-[0.12em] text-gray-900 mb-2">{title}</h3>
        <p className="text-sm text-gray-600 mb-4">{description}</p>
        <div className="space-y-2">
            {details.map((detail, i) => (
                <div key={i} className="flex items-start gap-2">
                    <span className="text-red-600 font-black mt-0.5">→</span>
                    <span className="text-sm text-gray-700">{detail}</span>
                </div>
            ))}
        </div>
    </div>
);

const FAQItem = ({ question, answer }: { question: string; answer: string }) => {
    const [isOpen, setIsOpen] = useState(false);
    return (
        <div className="bg-white rounded-[28px] border border-gray-100 overflow-hidden hover:border-gray-300 transition-colors">
            <button onClick={() => setIsOpen(!isOpen)} className="w-full px-8 py-6 flex justify-between items-center hover:bg-gray-50 transition-colors">
                <h4 className="text-sm font-black uppercase tracking-[0.12em] text-gray-900 text-left">{question}</h4>
                <span className={`text-red-600 font-black text-xl transition-transform ${isOpen ? "rotate-45" : ""}`}>+</span>
            </button>
            {isOpen && (
                <div className="px-8 pb-6 pt-0 border-t border-gray-100">
                    <p className="text-sm text-gray-600 leading-relaxed">{answer}</p>
                </div>
            )}
        </div>
    );
};

export default function KundtjanstPage() {
    const businessHours = [
        { day: "Måndag – Fredag", hours: "08:00 – 18:00" },
        { day: "Lördag", hours: "10:00 – 16:00" },
        { day: "Söndag", hours: "Stängt" },
        { day: "Helgdagar", hours: "Stängt" },
    ];

    const contactMethods = [
        {
            icon: "📞",
            title: "Telefonkontakt",
            description: "Tala direkt med vår kundtjänsthem",
            details: ["Telefon: 0771-900 900", "Vardagar 08:00 – 18:00", "Lördagar 10:00 – 16:00"],
            color: "bg-blue-50",
        },
        {
            icon: "✉️",
            title: "E-post",
            description: "Skicka oss ett meddelande",
            details: ["support@omegabank.se", "Svar inom 24 timmar", "Öppen 24/7"],
            color: "bg-green-50",
        },
        {
            icon: "💬",
            title: "Live Chat",
            description: "Chatta med vår support",
            details: ["Vardagar 08:00 – 18:00", "Instant svar", "Lösa problem snabbt"],
            color: "bg-purple-50",
        },
        {
            icon: "📍",
            title: "Besök oss",
            description: "Komma in personligt",
            details: ["Sveavägen 50, Stockholm", "Måndag – Fredag 09:00 – 17:00", "Ledig parkering tillgänglig"],
            color: "bg-red-50",
        },
    ];

    const faqs = [
        {
            question: "Hur lång tid tar det att få svar på min fråga?",
            answer: "Vi strävar efter att svara på alla frågor inom 24 timmar på vardagar. Via live chat får du svar direkt under öppettider. För brådskande ärenden rekommenderar vi att ringa oss.",
        },
        {
            question: "Kan jag få en ny PIN-kod om jag glömt bort den?",
            answer: "Ja, du kan enkelt återställa din PIN-kod genom att logga in på ditt konto och gå till Säkerhet. Om du behöver hjälp kontaktar du oss via telefon eller e-post.",
        },
        {
            question: "Vad gör jag om mitt kort är stulet?",
            answer: "Kontakta oss omedelbar via telefon på 0771-900 900 så blockerar vi kortet direkt. Du kan också blockera det själv i mobilappen under Mina kort.",
        },
        {
            question: "Hur långt i förväg kan jag ansöka om ett lån?",
            answer: "Du kan ansöka om lån när som helst via appen eller vår hemsida. Vi behandlar de flesta ansökningar inom 1-2 arbetsdagar.",
        },
        {
            question: "Kan jag ändra min lånets löptid?",
            answer: "Ja, du kan kontakta oss för att diskutera ändringar av löptid och betalningsvillkor. Vi kan ofta erbjuda flexibla lösningar anpassat efter dina behov.",
        },
        {
            question: "Vad kostar det att byta kreditkort?",
            answer: "Det är helt kostnadsfritt att byta till ett annat kreditkort. Vi hjälper dig genom hela processen utan extra avgifter.",
        },
    ];

    return (
        <main className="min-h-screen bg-[#f8fafc] text-gray-900 flex flex-col items-center">

            {/* Hero Section */}
            <header className="w-full bg-[#003349] px-8 py-24 relative overflow-hidden">
                <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-300 via-transparent to-transparent" />
                <div className="w-full max-w-6xl mx-auto relative z-10 mt-10">
                    <p className="text-red-500 font-black tracking-[0.4em] uppercase text-[10px] mb-4">Vi är här för dig</p>
                    <h1 className="text-4xl md:text-6xl font-black tracking-tighter text-white">Kundtjänst</h1>
                    <p className="text-blue-100/60 mt-4 text-lg font-medium max-w-xl">
                        Har du frågor eller behöver hjälp? Vårt dedikerade kundtjänstteam är här för att assistera dig.
                    </p>
                </div>
            </header>

            <div className="w-full max-w-6xl mx-auto px-8 py-12 space-y-12">

                {/* Open Hours */}
                <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                    <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-8">Öppettider</h2>
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
                        {businessHours.map((item, i) => (
                            <div key={i} className="flex flex-col justify-between items-center text-center p-6 bg-gradient-to-br from-gray-50 to-gray-100 rounded-2xl">
                                <p className="text-sm font-black uppercase tracking-widest text-gray-500 mb-3">{item.day}</p>
                                <p className={`text-2xl font-black ${item.hours === "Stängt" ? "text-red-600" : "text-[#003349]"}`}>
                                    {item.hours}
                                </p>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6 p-4 bg-blue-50 rounded-2xl border border-blue-100">
                        <p className="text-xs text-blue-900">
                            <strong>Notering:</strong> Under helgdagar kan svarssvar ta längre tid. Vi återkommer till ditt meddelande första arbetsdagen.
                        </p>
                    </div>
                </div>

                {/* Contact Methods */}
                <div>
                    <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-8">Kontakta oss</h2>
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        {contactMethods.map((method, i) => (
                            <ContactMethodCard key={i} {...method} />
                        ))}
                    </div>
                </div>

                {/* Quick Stats */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div className="bg-gradient-to-br from-red-600 to-red-700 rounded-[28px] p-8 text-white shadow-xl shadow-red-200/60">
                        <p className="text-[10px] font-black uppercase tracking-[0.2em] text-red-100 mb-2">Genomsnittlig svartstid</p>
                        <h3 className="text-3xl font-black">24 timmar</h3>
                        <p className="text-sm text-red-50 mt-2">Via e-post på vardagar</p>
                    </div>
                    <div className="bg-gradient-to-br from-blue-600 to-blue-700 rounded-[28px] p-8 text-white shadow-xl shadow-blue-200/60">
                        <p className="text-[10px] font-black uppercase tracking-[0.2em] text-blue-100 mb-2">Kundnöjdhet</p>
                        <h3 className="text-3xl font-black">4.8/5.0</h3>
                        <p className="text-sm text-blue-50 mt-2">Baserat på 2000+ recensioner</p>
                    </div>
                    <div className="bg-gradient-to-br from-[#003349] to-[#005070] rounded-[28px] p-8 text-white shadow-xl shadow-slate-300/60">
                        <p className="text-[10px] font-black uppercase tracking-[0.2em] text-slate-200 mb-2">Tillgänglighet</p>
                        <h3 className="text-3xl font-black">24/7</h3>
                        <p className="text-sm text-slate-100 mt-2">E-post och chat alltid öppen</p>
                    </div>
                </div>

                {/* FAQ Section */}
                <div>
                    <div className="mb-8">
                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-2">Vanliga frågor</h2>
                        <p className="text-sm text-gray-600">Här är svar på de frågor vi ofta får. Hittar du inte svaret? Kontakta oss!</p>
                    </div>
                    <div className="space-y-4">
                        {faqs.map((faq, i) => (
                            <FAQItem key={i} question={faq.question} answer={faq.answer} />
                        ))}
                    </div>
                </div>

                {/* CTA Section */}
                <div className="bg-gradient-to-r from-[#003349] to-[#004d6f] rounded-[28px] p-12 text-white relative overflow-hidden shadow-xl shadow-slate-300/60">
                    <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_bottom_left,_var(--tw-gradient-stops))] from-white via-transparent to-transparent" />
                    <div className="relative z-10 text-center">
                        <h3 className="text-2xl font-black uppercase tracking-tighter mb-4">Behöver du omedelbar hjälp?</h3>
                        <p className="text-white/80 mb-6 max-w-xl mx-auto">Ring oss på 0771-900 900 för brådskande ärenden. Vi är här för att hjälpa!</p>
                        <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
                            <a href="tel:0771900900" className="bg-red-600 hover:bg-red-700 transition-colors text-white font-black uppercase text-[11px] tracking-[0.15em] px-8 py-3 rounded-2xl">
                                Ring oss
                            </a>
                            <a href="mailto:support@omegabank.se" className="border-2 border-white hover:bg-white hover:text-[#003349] transition-colors text-white font-black uppercase text-[11px] tracking-[0.15em] px-8 py-3 rounded-2xl">
                                Skicka e-post
                            </a>
                        </div>
                    </div>
                </div>

                {/* Additional Info */}
                <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                    <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-6">Säkerhet och integritet</h2>
                    <p className="text-sm text-gray-600 mb-4">
                        Vi tar din säkerhet och integritet på största allvar. När du kontaktar oss hanteras all information enligt GDPR och gällande finansregleringar.
                    </p>
                    <ul className="space-y-2">
                        <li className="flex items-center gap-3 text-sm text-gray-700">
                            <span className="text-red-600 font-black">✓</span>
                            <span>All kommunikation är krypterad</span>
                        </li>
                        <li className="flex items-center gap-3 text-sm text-gray-700">
                            <span className="text-red-600 font-black">✓</span>
                            <span>Dina personuppgifter lagras säkert</span>
                        </li>
                        <li className="flex items-center gap-3 text-sm text-gray-700">
                            <span className="text-red-600 font-black">✓</span>
                            <span>Vi delar aldrig din information med tredje part</span>
                        </li>
                    </ul>
                </div>

            </div>
        </main>
    );
}


"use client";
import { useEffect, useState, useCallback } from "react";
import { isUserAdmin } from "@/utils/jwt";
import { useRouter } from "next/navigation";

const API = "http://localhost:8080";

interface UserPages {
    profile: { userId: string; firstName: string; lastName: string; socialSecurityNumber: string; address: string; city: string; zipCode: string; country: string; };
    loans: Loan[];
    creditCards: CreditCard[];
}
interface Loan { userId: string; loanId: string; loanType: string; amount: number; interestRate: number; status: string; }
interface CreditCard { userId: string; creditCardId: string; creditCardType: string; creditLimit: number; fee: number; interestRate: number; spentAmount: number; availableAmount: number; status: string; }
interface CreditCardTemplate { productId: string; creditCardType: string; creditLimit: number; fee: number; interestRate: number; productStatus: string; description: string; }
interface LoanTemplate { productId: string; loanType: string; interestRate: number; minAmount: number; maxAmount: number; productStatus: string; description: string; }

type Tab = "users" | "loans" | "creditcards" | "templates";

const StatusBadge = ({ status }: { status: string }) => (
    <span className={`text-[9px] font-black uppercase tracking-widest px-2 py-0.5 rounded-full ${status === "ACTIVE" ? "bg-green-50 text-green-600" : "bg-gray-100 text-gray-500"}`}>
        {status}
    </span>
);

const emptyLoanForm = { loanType: "", interestRate: "", minAmount: "", maxAmount: "", description: "", productStatus: "ACTIVE" };
const emptyCardForm = { creditCardType: "", creditLimit: "", fee: "", interestRate: "", description: "", productStatus: "ACTIVE" };

export default function AdminPanel() {
    const [isAdmin, setIsAdmin] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const [token, setToken] = useState<string | null>(null);
    const [activeTab, setActiveTab] = useState<Tab>("users");
    const router = useRouter();

    const [userId, setUserId] = useState("");
    const [userPages, setUserPages] = useState<UserPages | null>(null);
    const [userError, setUserError] = useState("");
    const [userLoading, setUserLoading] = useState(false);

    const [cardTemplates, setCardTemplates] = useState<CreditCardTemplate[]>([]);
    const [loanTemplates, setLoanTemplates] = useState<LoanTemplate[]>([]);
    const [templatesLoading, setTemplatesLoading] = useState(false);

    const [editingLoan, setEditingLoan] = useState<Loan | null>(null);
    const [editingCard, setEditingCard] = useState<CreditCard | null>(null);

    // Create template forms
    const [showCreateLoan, setShowCreateLoan] = useState(false);
    const [showCreateCard, setShowCreateCard] = useState(false);
    const [loanForm, setLoanForm] = useState(emptyLoanForm);
    const [cardForm, setCardForm] = useState(emptyCardForm);
    const [createLoanLoading, setCreateLoanLoading] = useState(false);
    const [createCardLoading, setCreateCardLoading] = useState(false);
    const [createLoanError, setCreateLoanError] = useState("");
    const [createCardError, setCreateCardError] = useState("");

    useEffect(() => {
        const t = localStorage.getItem("accessToken");
        if (!t || !isUserAdmin(t)) { router.push("/"); return; }
        setToken(t); setIsAdmin(true); setIsLoading(false);
    }, [router]);

    const authHeaders = useCallback(() => ({
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
    }), [token]);

    const fetchUser = async () => {
        if (!userId.trim()) return;
        setUserLoading(true); setUserError(""); setUserPages(null);
        try {
            const res = await fetch(`${API}/mypages/${userId}`, { headers: authHeaders() });
            if (!res.ok) { setUserError(`Ingen användare med ID ${userId} (${res.status})`); return; }
            setUserPages(await res.json());
        } catch { setUserError("Nätverksfel – kunde inte hämta användaren."); }
        finally { setUserLoading(false); }
    };

    const deleteLoan = async (loan: Loan) => {
        if (!confirm(`Radera lån ${loan.loanId}?`)) return;
        await fetch(`${API}/loan/${loan.userId}/${loan.loanType}/${loan.loanId}`, { method: "DELETE", headers: authHeaders() });
        if (userPages) setUserPages({ ...userPages, loans: userPages.loans.filter(l => l.loanId !== loan.loanId) });
    };

    const updateLoan = async () => {
        if (!editingLoan) return;
        await fetch(`${API}/loan/${editingLoan.userId}/${editingLoan.loanType}/${editingLoan.loanId}`, { method: "PUT", headers: authHeaders(), body: JSON.stringify({ status: editingLoan.status, amount: editingLoan.amount }) });
        setEditingLoan(null); fetchUser();
    };

    const deleteCreditCard = async (card: CreditCard) => {
        if (!confirm(`Radera kreditkort ${card.creditCardId}?`)) return;
        await fetch(`${API}/creditcard/${card.userId}/${card.creditCardType}/${card.creditCardId}`, { method: "DELETE", headers: authHeaders() });
        if (userPages) setUserPages({ ...userPages, creditCards: userPages.creditCards.filter(c => c.creditCardId !== card.creditCardId) });
    };

    const updateCreditCard = async () => {
        if (!editingCard) return;
        await fetch(`${API}/creditcard/${editingCard.userId}/${editingCard.creditCardType}/${editingCard.creditCardId}`, { method: "PUT", headers: authHeaders(), body: JSON.stringify({ status: editingCard.status, creditLimit: editingCard.creditLimit }) });
        setEditingCard(null); fetchUser();
    };

    const fetchTemplates = useCallback(async () => {
        setTemplatesLoading(true);
        try {
            const [cardRes, loanRes] = await Promise.all([fetch(`${API}/template/creditcards`), fetch(`${API}/template/loans`)]);
            if (cardRes.ok) setCardTemplates(await cardRes.json());
            if (loanRes.ok) setLoanTemplates(await loanRes.json());
        } finally { setTemplatesLoading(false); }
    }, []);

    useEffect(() => { if (activeTab === "templates" && token) fetchTemplates(); }, [activeTab, token, fetchTemplates]);

    const deleteCardTemplate = async (id: string) => {
        if (!confirm("Radera kortmall?")) return;
        await fetch(`${API}/template/creditcards/${id}`, { method: "DELETE", headers: authHeaders() });
        setCardTemplates(prev => prev.filter(t => t.productId !== id));
    };

    const deleteLoanTemplate = async (id: string) => {
        if (!confirm("Radera lånmall?")) return;
        await fetch(`${API}/template/loans/${id}`, { method: "DELETE", headers: authHeaders() });
        setLoanTemplates(prev => prev.filter(t => t.productId !== id));
    };

    const toggleCardTemplate = async (t: CreditCardTemplate) => {
        const newStatus = t.productStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
        await fetch(`${API}/template/creditcards/${t.productId}`, { method: "PUT", headers: authHeaders(), body: JSON.stringify({ productStatus: newStatus }) });
        setCardTemplates(prev => prev.map(c => c.productId === t.productId ? { ...c, productStatus: newStatus } : c));
    };

    const toggleLoanTemplate = async (t: LoanTemplate) => {
        const newStatus = t.productStatus === "ACTIVE" ? "INACTIVE" : "ACTIVE";
        await fetch(`${API}/template/loans/${t.productId}`, { method: "PUT", headers: authHeaders(), body: JSON.stringify({ productStatus: newStatus }) });
        setLoanTemplates(prev => prev.map(l => l.productId === t.productId ? { ...l, productStatus: newStatus } : l));
    };

    // --- CREATE LOAN TEMPLATE ---
    const createLoanTemplate = async () => {
        setCreateLoanError("");
        if (!loanForm.loanType.trim()) { setCreateLoanError("Låntyp är obligatoriskt."); return; }
        if (!loanForm.interestRate || !loanForm.minAmount || !loanForm.maxAmount) { setCreateLoanError("Ränta, min- och maxbelopp är obligatoriska."); return; }
        setCreateLoanLoading(true);
        try {
            const res = await fetch(`${API}/template/loans`, {
                method: "POST",
                headers: authHeaders(),
                body: JSON.stringify({
                    loanType: loanForm.loanType.trim(),
                    interestRate: parseFloat(loanForm.interestRate),
                    minAmount: parseInt(loanForm.minAmount),
                    maxAmount: parseInt(loanForm.maxAmount),
                    description: loanForm.description.trim(),
                    productStatus: loanForm.productStatus,
                }),
            });
            if (!res.ok) { setCreateLoanError(`Fel från servern: ${res.status}`); return; }
            const created = await res.json();
            setLoanTemplates(prev => [...prev, created]);
            setLoanForm(emptyLoanForm);
            setShowCreateLoan(false);
        } catch { setCreateLoanError("Nätverksfel – kunde inte skapa mallen."); }
        finally { setCreateLoanLoading(false); }
    };

    // --- CREATE CREDIT CARD TEMPLATE ---
    const createCardTemplate = async () => {
        setCreateCardError("");
        if (!cardForm.creditCardType.trim()) { setCreateCardError("Korttyp är obligatoriskt."); return; }
        if (!cardForm.creditLimit || !cardForm.fee || !cardForm.interestRate) { setCreateCardError("Kreditgräns, avgift och ränta är obligatoriska."); return; }
        setCreateCardLoading(true);
        try {
            const res = await fetch(`${API}/template/creditcards`, {
                method: "POST",
                headers: authHeaders(),
                body: JSON.stringify({
                    creditCardType: cardForm.creditCardType.trim(),
                    creditLimit: parseInt(cardForm.creditLimit),
                    fee: parseInt(cardForm.fee),
                    interestRate: parseFloat(cardForm.interestRate),
                    description: cardForm.description.trim(),
                    productStatus: cardForm.productStatus,
                }),
            });
            if (!res.ok) { setCreateCardError(`Fel från servern: ${res.status}`); return; }
            const created = await res.json();
            setCardTemplates(prev => [...prev, created]);
            setCardForm(emptyCardForm);
            setShowCreateCard(false);
        } catch { setCreateCardError("Nätverksfel – kunde inte skapa mallen."); }
        finally { setCreateCardLoading(false); }
    };

    if (isLoading) return (
        <div className="p-20 text-center font-black tracking-[0.3em] uppercase text-[10px] text-white bg-[#003349] min-h-screen flex items-center justify-center">
            Laddar Admin Panel...
        </div>
    );
    if (!isAdmin) return null;

    const tabs: { id: Tab; label: string; icon: string }[] = [
        { id: "users", label: "Användare", icon: "👤" },
        { id: "loans", label: "Lån", icon: "🏦" },
        { id: "creditcards", label: "Kreditkort", icon: "💳" },
        { id: "templates", label: "Produktmallar", icon: "📋" },
    ];
    const needsSearch = activeTab === "users" || activeTab === "loans" || activeTab === "creditcards";

    return (
        <main className="min-h-screen bg-[#f8fafc] text-gray-900 flex flex-col items-center">
            {/* Hero */}
            <header className="w-full bg-[#003349] px-8 py-24 relative overflow-hidden">
                <div className="absolute inset-0 opacity-10 pointer-events-none bg-[radial-gradient(circle_at_top_right,_var(--tw-gradient-stops))] from-blue-300 via-transparent to-transparent" />
                <div className="w-full max-w-6xl mx-auto relative z-10 mt-10">
                    <p className="text-red-500 font-black tracking-[0.4em] uppercase text-[10px] mb-4">Administration</p>
                    <h1 className="text-4xl md:text-6xl font-black tracking-tighter text-white">Admin Panel.</h1>
                    <p className="text-blue-100/60 mt-4 text-lg font-medium max-w-xl">
                        Hantera användare, lån, kreditkort och produktmallar.
                    </p>
                </div>
            </header>

            {/* Tab bar */}
            <div className="w-full bg-white border-b border-gray-100 sticky top-[72px] z-40 shadow-sm">
                <div className="max-w-6xl mx-auto px-8 flex">
                    {tabs.map(tab => (
                        <button key={tab.id} onClick={() => setActiveTab(tab.id)}
                                className={`flex items-center gap-2 px-5 py-4 text-[11px] font-black uppercase tracking-[0.15em] border-b-2 transition-all ${
                                    activeTab === tab.id ? "border-red-600 text-red-600" : "border-transparent text-gray-400 hover:text-[#003349]"
                                }`}>
                            {tab.icon} {tab.label}
                        </button>
                    ))}
                </div>
            </div>

            <div className="w-full max-w-6xl mx-auto px-8 py-12 space-y-6">
                {/* Search box */}
                {needsSearch && (
                    <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-6">Sök användare</h2>
                        <div className="flex gap-3">
                            <input type="text" value={userId}
                                   onChange={e => setUserId(e.target.value)}
                                   onKeyDown={e => e.key === "Enter" && fetchUser()}
                                   placeholder="Ange användar-ID, t.ex. 4"
                                   className="flex-1 border-2 border-gray-200 rounded-2xl px-5 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors" />
                            <button onClick={fetchUser} disabled={userLoading}
                                    className="bg-[#003349] text-white font-black text-[11px] uppercase tracking-[0.15em] px-8 py-3 rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98] disabled:opacity-50">
                                {userLoading ? "Söker..." : "Hämta"}
                            </button>
                        </div>
                        {userError && <p className="mt-3 text-sm text-red-600 bg-red-50 px-4 py-2 rounded-xl">{userError}</p>}
                    </div>
                )}

                {/* USERS */}
                {activeTab === "users" && userPages && (
                    <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                        <div className="flex justify-between items-start mb-8">
                            <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400">Profil</h2>
                            <div className="flex gap-2">
                                <span className="text-[9px] font-black uppercase tracking-widest bg-blue-50 text-[#003349] px-3 py-1 rounded-full">
                                    {userPages.loans?.length ?? 0} lån</span>
                                <span className="text-[9px] font-black uppercase tracking-widest bg-red-50 text-red-600 px-3 py-1 rounded-full">
                                    {userPages.creditCards?.length ?? 0} kort</span>
                            </div>
                        </div>
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
                            {[
                                { label: "User ID", val: userPages.profile.userId },
                                { label: "Förnamn", val: userPages.profile.firstName },
                                { label: "Efternamn", val: userPages.profile.lastName },
                                { label: "Personnummer", val: userPages.profile.socialSecurityNumber },
                                { label: "Adress", val: userPages.profile.address },
                                { label: "Stad", val: userPages.profile.city },
                                { label: "Postnr", val: userPages.profile.zipCode },
                                { label: "Land", val: userPages.profile.country },
                            ].map(f => (
                                <div key={f.label}>
                                    <p className="text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1">{f.label}</p>
                                    <p className="font-bold text-gray-900 text-sm">{f.val || "–"}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
                {activeTab === "users" && !userPages && !userLoading && (
                    <p className="text-gray-400 italic text-sm px-2">Sök en användare ovan för att se deras profil.</p>
                )}

                {/* LOANS */}
                {activeTab === "loans" && userPages && (
                    <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-6">
                            Lån – {userPages.profile.firstName} {userPages.profile.lastName}
                        </h2>
                        {(userPages.loans?.length ?? 0) === 0 ? (
                            <p className="text-gray-400 italic text-sm">Inga lån hittades.</p>
                        ) : (
                            <div className="space-y-3">
                                {(userPages.loans ?? []).map(loan => (
                                    <div key={loan.loanId} className="group flex items-center justify-between border border-gray-100 rounded-2xl px-6 py-4 hover:border-gray-300 transition-colors">
                                        <div className="space-y-2">
                                            <div className="flex items-center gap-3">
                                                <p className="font-black text-sm uppercase tracking-tight">{loan.loanType}</p>
                                                <span className="text-[9px] text-gray-400 font-mono">#{loan.loanId}</span>
                                                <StatusBadge status={loan.status} />
                                            </div>
                                            <p className="text-xs text-gray-500">
                                                Belopp: <strong className="text-gray-800">{loan.amount?.toLocaleString("sv-SE")} kr</strong>
                                                <span className="mx-2 text-gray-300">|</span>
                                                Ränta: <strong className="text-gray-800">{loan.interestRate}%</strong>
                                            </p>
                                        </div>
                                        <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                            <button onClick={() => setEditingLoan(loan)} className="text-[10px] font-black uppercase tracking-wider border-2 border-gray-200 px-4 py-2 rounded-xl hover:border-[#003349] hover:text-[#003349] transition-colors">Redigera</button>
                                            <button onClick={() => deleteLoan(loan)} className="text-[10px] font-black uppercase tracking-wider bg-red-600 text-white px-4 py-2 rounded-xl hover:bg-red-700 transition-colors">Radera</button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                )}
                {activeTab === "loans" && !userPages && !userLoading && (
                    <p className="text-gray-400 italic text-sm px-2">Sök en användare ovan för att se deras lån.</p>
                )}

                {/* CREDIT CARDS */}
                {activeTab === "creditcards" && userPages && (
                    <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400 mb-6">
                            Kreditkort – {userPages.profile.firstName} {userPages.profile.lastName}
                        </h2>
                        {(userPages.creditCards?.length ?? 0) === 0 ? (
                            <p className="text-gray-400 italic text-sm">Inga kreditkort hittades.</p>
                        ) : (
                            <div className="space-y-3">
                                {(userPages.creditCards ?? []).map(card => (
                                    <div key={card.creditCardId} className="group flex items-center justify-between border border-gray-100 rounded-2xl px-6 py-4 hover:border-gray-300 transition-colors">
                                        <div className="space-y-2">
                                            <div className="flex items-center gap-3">
                                                <p className="font-black text-sm uppercase tracking-tight">{card.creditCardType}</p>
                                                <span className="text-[9px] text-gray-400 font-mono">#{card.creditCardId}</span>
                                                <StatusBadge status={card.status} />
                                            </div>
                                            <p className="text-xs text-gray-500">
                                                Limit: <strong className="text-gray-800">{card.creditLimit?.toLocaleString("sv-SE")} kr</strong>
                                                <span className="mx-2 text-gray-300">|</span>
                                                Spenderat: <strong className="text-gray-800">{card.spentAmount?.toLocaleString("sv-SE")} kr</strong>
                                                <span className="mx-2 text-gray-300">|</span>
                                                Kvar: <strong className="text-gray-800">{card.availableAmount?.toLocaleString("sv-SE")} kr</strong>
                                            </p>
                                            <div className="w-48 bg-gray-100 h-1 rounded-full overflow-hidden">
                                                <div className="bg-red-600 h-full rounded-full" style={{ width: `${Math.min(((card.spentAmount ?? 0) / (card.creditLimit || 1)) * 100, 100)}%` }} />
                                            </div>
                                        </div>
                                        <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                            <button onClick={() => setEditingCard(card)} className="text-[10px] font-black uppercase tracking-wider border-2 border-gray-200 px-4 py-2 rounded-xl hover:border-[#003349] hover:text-[#003349] transition-colors">Redigera</button>
                                            <button onClick={() => deleteCreditCard(card)} className="text-[10px] font-black uppercase tracking-wider bg-red-600 text-white px-4 py-2 rounded-xl hover:bg-red-700 transition-colors">Radera</button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                )}
                {activeTab === "creditcards" && !userPages && !userLoading && (
                    <p className="text-gray-400 italic text-sm px-2">Sök en användare ovan för att se deras kreditkort.</p>
                )}

                {/* TEMPLATES */}
                {activeTab === "templates" && (
                    <div className="space-y-6">
                        {templatesLoading ? <p className="text-gray-400 italic text-sm px-2">Laddar mallar...</p> : (
                            <>
                                {/* ── KREDITKORTMALLAR ── */}
                                <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                                    <div className="flex justify-between items-center mb-8">
                                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400">Kreditkortmallar</h2>
                                        <div className="flex items-center gap-3">
                                            <div className="p-3 bg-red-50 rounded-2xl text-red-600">
                                                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2.5"><path d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z" /></svg>
                                            </div>
                                            <button
                                                onClick={() => { setShowCreateCard(v => !v); setCreateCardError(""); }}
                                                className="flex items-center gap-2 bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] px-5 py-2.5 rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98]"
                                            >
                                                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
                                                Ny kortmall
                                            </button>
                                        </div>
                                    </div>

                                    {/* Create card form */}
                                    {showCreateCard && (
                                        <div className="mb-8 border-2 border-dashed border-gray-200 rounded-2xl p-6 bg-gray-50/50">
                                            <p className="text-[10px] font-black uppercase tracking-[0.2em] text-gray-400 mb-5">Ny kreditkortmall</p>
                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Korttyp *</label>
                                                    <input
                                                        type="text"
                                                        placeholder="t.ex. PREMIUM"
                                                        value={cardForm.creditCardType}
                                                        onChange={e => setCardForm({ ...cardForm, creditCardType: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Kreditgräns (kr) *</label>
                                                    <input
                                                        type="number"
                                                        placeholder="t.ex. 50000"
                                                        value={cardForm.creditLimit}
                                                        onChange={e => setCardForm({ ...cardForm, creditLimit: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Årsavgift (kr) *</label>
                                                    <input
                                                        type="number"
                                                        placeholder="t.ex. 495"
                                                        value={cardForm.fee}
                                                        onChange={e => setCardForm({ ...cardForm, fee: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Ränta (%) *</label>
                                                    <input
                                                        type="number"
                                                        step="0.01"
                                                        placeholder="t.ex. 19.9"
                                                        value={cardForm.interestRate}
                                                        onChange={e => setCardForm({ ...cardForm, interestRate: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div className="md:col-span-2">
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Beskrivning</label>
                                                    <input
                                                        type="text"
                                                        placeholder="Kort beskrivning av produkten"
                                                        value={cardForm.description}
                                                        onChange={e => setCardForm({ ...cardForm, description: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Status</label>
                                                    <select
                                                        value={cardForm.productStatus}
                                                        onChange={e => setCardForm({ ...cardForm, productStatus: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    >
                                                        <option value="ACTIVE">ACTIVE</option>
                                                        <option value="INACTIVE">INACTIVE</option>
                                                    </select>
                                                </div>
                                            </div>
                                            {createCardError && <p className="mt-3 text-sm text-red-600 bg-red-50 px-4 py-2 rounded-xl">{createCardError}</p>}
                                            <div className="flex gap-3 mt-5">
                                                <button
                                                    onClick={createCardTemplate}
                                                    disabled={createCardLoading}
                                                    className="bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] px-8 py-3 rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98] disabled:opacity-50"
                                                >
                                                    {createCardLoading ? "Skapar..." : "Skapa mall"}
                                                </button>
                                                <button
                                                    onClick={() => { setShowCreateCard(false); setCardForm(emptyCardForm); setCreateCardError(""); }}
                                                    className="border-2 border-gray-200 font-black text-[10px] uppercase tracking-[0.15em] px-8 py-3 rounded-2xl hover:border-gray-400 transition-colors"
                                                >
                                                    Avbryt
                                                </button>
                                            </div>
                                        </div>
                                    )}

                                    <div className="space-y-3">
                                        {cardTemplates.length === 0 && !showCreateCard && (
                                            <p className="text-gray-400 italic text-sm">Inga kortmallar finns ännu.</p>
                                        )}
                                        {cardTemplates.map(t => (
                                            <div key={t.productId} className="group flex items-center justify-between border border-gray-100 rounded-2xl px-6 py-4 hover:border-gray-300 transition-colors">
                                                <div className="space-y-2">
                                                    <div className="flex items-center gap-3">
                                                        <p className="font-black text-sm uppercase tracking-tight">{t.creditCardType}</p>
                                                        <span className="text-[9px] text-gray-400 font-mono">#{t.productId}</span>
                                                        <StatusBadge status={t.productStatus} />
                                                    </div>
                                                    <p className="text-xs text-gray-500">
                                                        Limit: <strong className="text-gray-800">{t.creditLimit?.toLocaleString("sv-SE")} kr</strong>
                                                        <span className="mx-2 text-gray-300">|</span>
                                                        Avgift: <strong className="text-gray-800">{t.fee} kr</strong>
                                                        <span className="mx-2 text-gray-300">|</span>
                                                        Ränta: <strong className="text-gray-800">{t.interestRate}%</strong>
                                                    </p>
                                                </div>
                                                <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                                    <button onClick={() => toggleCardTemplate(t)} className="text-[10px] font-black uppercase tracking-wider border-2 border-gray-200 px-4 py-2 rounded-xl hover:border-[#003349] hover:text-[#003349] transition-colors">
                                                        {t.productStatus === "ACTIVE" ? "Inaktivera" : "Aktivera"}
                                                    </button>
                                                    <button onClick={() => deleteCardTemplate(t.productId)} className="text-[10px] font-black uppercase tracking-wider bg-red-600 text-white px-4 py-2 rounded-xl hover:bg-red-700 transition-colors">Radera</button>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>

                                {/* ── LÅNMALLAR ── */}
                                <div className="bg-white rounded-[28px] p-8 shadow-xl shadow-gray-200/60 border border-gray-100">
                                    <div className="flex justify-between items-center mb-8">
                                        <h2 className="text-[11px] font-black uppercase tracking-[0.2em] text-gray-400">Lånmallar</h2>
                                        <div className="flex items-center gap-3">
                                            <div className="p-3 bg-blue-50 rounded-2xl text-[#003349]">
                                                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="2.5"><path d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" /></svg>
                                            </div>
                                            <button
                                                onClick={() => { setShowCreateLoan(v => !v); setCreateLoanError(""); }}
                                                className="flex items-center gap-2 bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] px-5 py-2.5 rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98]"
                                            >
                                                <svg className="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24" strokeWidth="3"><path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" /></svg>
                                                Ny lånmall
                                            </button>
                                        </div>
                                    </div>

                                    {/* Create loan form */}
                                    {showCreateLoan && (
                                        <div className="mb-8 border-2 border-dashed border-gray-200 rounded-2xl p-6 bg-gray-50/50">
                                            <p className="text-[10px] font-black uppercase tracking-[0.2em] text-gray-400 mb-5">Ny lånmall</p>
                                            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Låntyp *</label>
                                                    <input
                                                        type="text"
                                                        placeholder="t.ex. BOLÅN"
                                                        value={loanForm.loanType}
                                                        onChange={e => setLoanForm({ ...loanForm, loanType: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Ränta (%) *</label>
                                                    <input
                                                        type="number"
                                                        step="0.01"
                                                        placeholder="t.ex. 3.5"
                                                        value={loanForm.interestRate}
                                                        onChange={e => setLoanForm({ ...loanForm, interestRate: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Minbelopp (kr) *</label>
                                                    <input
                                                        type="number"
                                                        placeholder="t.ex. 10000"
                                                        value={loanForm.minAmount}
                                                        onChange={e => setLoanForm({ ...loanForm, minAmount: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Maxbelopp (kr) *</label>
                                                    <input
                                                        type="number"
                                                        placeholder="t.ex. 5000000"
                                                        value={loanForm.maxAmount}
                                                        onChange={e => setLoanForm({ ...loanForm, maxAmount: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div className="md:col-span-2">
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Beskrivning</label>
                                                    <input
                                                        type="text"
                                                        placeholder="Kort beskrivning av produkten"
                                                        value={loanForm.description}
                                                        onChange={e => setLoanForm({ ...loanForm, description: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    />
                                                </div>
                                                <div>
                                                    <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-1.5">Status</label>
                                                    <select
                                                        value={loanForm.productStatus}
                                                        onChange={e => setLoanForm({ ...loanForm, productStatus: e.target.value })}
                                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors bg-white"
                                                    >
                                                        <option value="ACTIVE">ACTIVE</option>
                                                        <option value="INACTIVE">INACTIVE</option>
                                                    </select>
                                                </div>
                                            </div>
                                            {createLoanError && <p className="mt-3 text-sm text-red-600 bg-red-50 px-4 py-2 rounded-xl">{createLoanError}</p>}
                                            <div className="flex gap-3 mt-5">
                                                <button
                                                    onClick={createLoanTemplate}
                                                    disabled={createLoanLoading}
                                                    className="bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] px-8 py-3 rounded-2xl hover:bg-red-600 transition-all active:scale-[0.98] disabled:opacity-50"
                                                >
                                                    {createLoanLoading ? "Skapar..." : "Skapa mall"}
                                                </button>
                                                <button
                                                    onClick={() => { setShowCreateLoan(false); setLoanForm(emptyLoanForm); setCreateLoanError(""); }}
                                                    className="border-2 border-gray-200 font-black text-[10px] uppercase tracking-[0.15em] px-8 py-3 rounded-2xl hover:border-gray-400 transition-colors"
                                                >
                                                    Avbryt
                                                </button>
                                            </div>
                                        </div>
                                    )}

                                    <div className="space-y-3">
                                        {loanTemplates.length === 0 && !showCreateLoan && (
                                            <p className="text-gray-400 italic text-sm">Inga lånmallar finns ännu.</p>
                                        )}
                                        {loanTemplates.map(t => (
                                            <div key={t.productId} className="group flex items-center justify-between border border-gray-100 rounded-2xl px-6 py-4 hover:border-gray-300 transition-colors">
                                                <div className="space-y-2">
                                                    <div className="flex items-center gap-3">
                                                        <p className="font-black text-sm uppercase tracking-tight">{t.loanType}</p>
                                                        <span className="text-[9px] text-gray-400 font-mono">#{t.productId}</span>
                                                        <StatusBadge status={t.productStatus} />
                                                    </div>
                                                    <p className="text-xs text-gray-500">
                                                        Min: <strong className="text-gray-800">{t.minAmount?.toLocaleString("sv-SE")} kr</strong>
                                                        <span className="mx-2 text-gray-300">|</span>
                                                        Max: <strong className="text-gray-800">{t.maxAmount?.toLocaleString("sv-SE")} kr</strong>
                                                        <span className="mx-2 text-gray-300">|</span>
                                                        Ränta: <strong className="text-gray-800">{t.interestRate}%</strong>
                                                    </p>
                                                </div>
                                                <div className="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                                                    <button onClick={() => toggleLoanTemplate(t)} className="text-[10px] font-black uppercase tracking-wider border-2 border-gray-200 px-4 py-2 rounded-xl hover:border-[#003349] hover:text-[#003349] transition-colors">
                                                        {t.productStatus === "ACTIVE" ? "Inaktivera" : "Aktivera"}
                                                    </button>
                                                    <button onClick={() => deleteLoanTemplate(t.productId)} className="text-[10px] font-black uppercase tracking-wider bg-red-600 text-white px-4 py-2 rounded-xl hover:bg-red-700 transition-colors">Radera</button>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                )}
            </div>

            {/* EDIT LOAN MODAL */}
            {editingLoan && (
                <div className="fixed inset-0 bg-[#003349]/60 backdrop-blur-sm flex items-center justify-center z-50">
                    <div className="bg-white rounded-[28px] p-8 w-96 shadow-2xl">
                        <h3 className="font-black text-sm uppercase tracking-[0.2em] text-gray-400 mb-6">Redigera lån</h3>
                        <div className="space-y-4">
                            <div>
                                <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-2">Status</label>
                                <select value={editingLoan.status} onChange={e => setEditingLoan({ ...editingLoan, status: e.target.value })}
                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors">
                                    <option>ACTIVE</option><option>INACTIVE</option><option>PENDING</option><option>REJECTED</option>
                                </select>
                            </div>
                            <div>
                                <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-2">Belopp (kr)</label>
                                <input type="number" value={editingLoan.amount} onChange={e => setEditingLoan({ ...editingLoan, amount: Number(e.target.value) })}
                                       className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors" />
                            </div>
                        </div>
                        <div className="flex gap-3 mt-6">
                            <button onClick={updateLoan} className="flex-1 bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] py-3 rounded-2xl hover:bg-red-600 transition-all">Spara</button>
                            <button onClick={() => setEditingLoan(null)} className="flex-1 border-2 border-gray-200 font-black text-[10px] uppercase tracking-[0.15em] py-3 rounded-2xl hover:border-gray-400 transition-colors">Avbryt</button>
                        </div>
                    </div>
                </div>
            )}

            {/* EDIT CARD MODAL */}
            {editingCard && (
                <div className="fixed inset-0 bg-[#003349]/60 backdrop-blur-sm flex items-center justify-center z-50">
                    <div className="bg-white rounded-[28px] p-8 w-96 shadow-2xl">
                        <h3 className="font-black text-sm uppercase tracking-[0.2em] text-gray-400 mb-6">Redigera kreditkort</h3>
                        <div className="space-y-4">
                            <div>
                                <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-2">Status</label>
                                <select value={editingCard.status} onChange={e => setEditingCard({ ...editingCard, status: e.target.value })}
                                        className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors">
                                    <option>ACTIVE</option><option>INACTIVE</option><option>BLOCKED</option>
                                </select>
                            </div>
                            <div>
                                <label className="block text-[10px] font-black uppercase tracking-widest text-gray-400 mb-2">Kreditgräns (kr)</label>
                                <input type="number" value={editingCard.creditLimit} onChange={e => setEditingCard({ ...editingCard, creditLimit: Number(e.target.value) })}
                                       className="w-full border-2 border-gray-200 rounded-2xl px-4 py-3 text-sm focus:outline-none focus:border-[#003349] transition-colors" />
                            </div>
                        </div>
                        <div className="flex gap-3 mt-6">
                            <button onClick={updateCreditCard} className="flex-1 bg-[#003349] text-white font-black text-[10px] uppercase tracking-[0.15em] py-3 rounded-2xl hover:bg-red-600 transition-all">Spara</button>
                            <button onClick={() => setEditingCard(null)} className="flex-1 border-2 border-gray-200 font-black text-[10px] uppercase tracking-[0.15em] py-3 rounded-2xl hover:border-gray-400 transition-colors">Avbryt</button>
                        </div>
                    </div>
                </div>
            )}
        </main>
    );
}
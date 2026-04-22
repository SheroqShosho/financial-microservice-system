const Footer = () => {
  return (
    <footer className="w-full bg-gray-50 border-t border-gray-200 py-12 px-8 mt-auto">
      <div className="max-w-7xl mx-auto grid grid-cols-1 md:grid-cols-3 gap-8">
        <div>
          <h3 className="font-bold mb-4 text-gray-900">Omega Bank</h3>
          <p className="text-sm text-gray-600">Get rich or die trying.</p>
        </div>
        <div>
          <h3 className="font-bold mb-4 text-gray-900">Snabblänkar</h3>
          <ul className="text-sm text-gray-600 space-y-2">
            <li><a href="#" className="hover:underline">Om oss</a></li>
            <li><a href="#" className="hover:underline">Säkerhet</a></li>
            <li><a href="#" className="hover:underline">Cookies</a></li>
          </ul>
        </div>
        <div>
          <h3 className="font-bold mb-4 text-gray-900">Kontakt</h3>
          <p className="text-sm text-gray-600">Tel: 0771-32 32 32</p>
          <p className="text-sm text-gray-600 italic mt-2">Öppet dygnet runt för spärrning av kort.</p>
        </div>
      </div>
      <div className="max-w-7xl mx-auto mt-12 pt-8 border-t border-gray-200 text-xs text-gray-500">
        © {new Date().getFullYear()} Omega Bank AB (publ).
      </div>
    </footer>
  );
};

export default Footer;